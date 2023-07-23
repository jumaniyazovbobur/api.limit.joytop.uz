package uz.dachatop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.dachatop.dto.sms.SmsDTO;
import uz.dachatop.dto.sms.SmsHttpResponseDTO;
import uz.dachatop.dto.sms.SmsRequestDTO;
import uz.dachatop.dto.sms.SmsResponseDTO;
import uz.dachatop.entity.SmsEntity;
import uz.dachatop.entity.SmsTokenEntity;
import uz.dachatop.enums.SmsStatus;
import uz.dachatop.exp.AppBadRequestException;
import uz.dachatop.repository.SmsRepository;
import uz.dachatop.util.RandomUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService {

    @Value("${sms.url}")
    private String smsUrl;

    @Value("${sms.fly.uz.url}")
    private String smsFlyUrl;
    @Value("${sms.key}")
    private String key;
    @Value("${my.eskiz.uz.email}")
    private String myEskizUzEmail;

    @Value("${my.eskiz.uz.password}")
    private String myEskizUzPassword;

    private final SmsRepository smsRepository;

    public void sendRegistrationSmsCode(String phone) {
        SmsEntity sms = new SmsEntity();
        String code = RandomUtil.getRandomSmsCode();
        String message = "Joytop partali uchun\n registratsiya kodi: " + code;
        send(phone, message); // send sms
        sms.setContent(code);
        sms.setPhone(phone);
        sms.setStatus(SmsStatus.NOT_USED);
        smsRepository.save(sms);
    }

    public void sendConfirmSmsCode(String phone) {
        SmsEntity sms = new SmsEntity();
        String code = RandomUtil.getRandomSmsCode();
        String message = "Joytop partali\n tastiklash kodi: " + code;
        send(phone, message); // send sms
        sms.setContent(code);
        sms.setPhone(phone);
        sms.setStatus(SmsStatus.NOT_USED);
        smsRepository.save(sms);
    }

    public void sendResetPasswordSmsCode(String phone, String tempPassword) {
        SmsEntity sms = new SmsEntity();
        String message = "Joytop partali\n Vaqtinchalik parol: " + tempPassword;
        send(phone, message); // send sms
        sms.setContent(tempPassword);
        sms.setPhone(phone);
        sms.setStatus(SmsStatus.NOT_USED);
        smsRepository.save(sms);
    }

    public String sendSms(String phone) {
        SmsEntity sms = new SmsEntity();
        String code = RandomUtil.getRandomSmsCode();
        String message = "Joytop partali uchun\n registratsiya kodi: " + code;
        send(phone, message);
        sms.setContent(code);
        sms.setPhone(phone);
        sms.setStatus(SmsStatus.NOT_USED);
        smsRepository.save(sms);
        return sms.getId();
    }

    private SmsResponseDTO send(String phone, String message) {
        String token = "Bearer " + getSmsToken();
        String prPhone = phone;
        if (prPhone.startsWith("+")) {
            prPhone = prPhone.substring(1);
        }
        OkHttpClient client = new OkHttpClient();
        SmsRequestDTO requestDTO = new SmsRequestDTO();

//        String code = RandomUtil.getRandomSmsCode();
        requestDTO.setKey(key);
        requestDTO.setPhone(phone);
        requestDTO.setContent(message);


        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("mobile_phone", prPhone)
                .addFormDataPart("message", message)
                .addFormDataPart("from", "4546")

                .build();
        Request request = new Request.Builder()
                .url(smsUrl + "api/message/sms/send")
                .method("POST", body)
                .header("Authorization", token)
                .build();

        Thread thread = new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    System.out.println(response);
                } else {
                    log.info("Sms Token Exception");
                    throw new IOException();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        });

        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = (th, ex) -> {
            ex.printStackTrace();
        };
        thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        thread.start();
        System.out.println("SMS Code  => " + "response");
        return null;
    }

    public void confirmSmsCode(String phone, String code) {
        Optional<SmsEntity> optional = smsRepository.findTopByPhoneAndStatusOrderByCreatedDateDesc(phone, SmsStatus.NOT_USED);
        if (optional.isEmpty()) {
            throw new AppBadRequestException("Code not found");
        }

        SmsEntity smsEntity = optional.get();
        if (!smsEntity.getContent().equals(code)) {
            smsRepository.updateSmsStatus(SmsStatus.INVALID, smsEntity.getId());
            throw new AppBadRequestException("Code wrong");
        }
        LocalDateTime extTime = smsEntity.getCreatedDate().plusMinutes(2);
        if (LocalDateTime.now().isAfter(extTime)) {
            smsRepository.updateSmsStatus(SmsStatus.INVALID, smsEntity.getId());
            throw new AppBadRequestException("Time is up");
        }
        smsRepository.updateSmsStatus(SmsStatus.USED, smsEntity.getId());
    }

    private void sendSms(String phone, String message) {
        RestTemplate restTemplate = new RestTemplate();

        SmsDTO dto = new SmsDTO();
        dto.setKey(key);
        dto.setMessage(message);
        dto.setPhone(phone);

        HttpEntity<SmsDTO> requestBody = new HttpEntity<SmsDTO>(dto);
        SmsHttpResponseDTO response = restTemplate.postForObject(smsFlyUrl, requestBody, SmsHttpResponseDTO.class);
        log.info("Sms sent: request {}, response {}", dto, response);
    }

    private SmsResponseDTO toDTO(SmsEntity entity) {
        SmsResponseDTO sms = new SmsResponseDTO();
        sms.setId(entity.getId());
        sms.setContent(entity.getContent());
        sms.setPhone(entity.getPhone());
        sms.setCreatedDate(entity.getCreatedDate());
        sms.setVisible(entity.getVisible());
        return sms;
    }

    private String getSmsToken() {
        SmsEntity smsToken = smsRepository.findByPhone(myEskizUzEmail);
        if (smsToken == null) {
            String token = myEskizUzLogin();
            SmsTokenEntity smsEntity = new SmsTokenEntity();//TODO tokin
            smsEntity.setEmail(myEskizUzEmail);

            smsEntity.setToken(token);
//            smsRepository.save(smsEntity);
            return token;
        }
        if (!smsToken.getCreatedDate().plusDays(25).equals(LocalDate.now())) {
            return smsToken.getContent();
        } else { // REFRESH TOKEN
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url(smsUrl + "api/message/sms/send")
                    .method("PATCH", body)
                    .header("Authorization", "Bearer " + smsToken.getContent())
                    .build();

            Response response;
            try {
                response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    log.info("Sms Token Exception");
                    throw new IOException();
                } else {
                    return smsToken.getContent();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }

    }

    public String myEskizUzLogin() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("email", myEskizUzEmail)
                .addFormDataPart("password", myEskizUzPassword)
                .build();
        Request request = new Request.Builder()
                .url(smsUrl + "api/auth/login")
                .method("POST", body)
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                log.info("Sms Token Exeption");
                throw new IOException();
            } else {
                JSONObject object = new JSONObject(response.body().string());
                JSONObject data = object.getJSONObject("data");
                Object token = data.get("token");
                return token.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }
}


