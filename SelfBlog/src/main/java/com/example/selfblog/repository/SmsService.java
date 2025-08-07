package com.example.selfblog.repository;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
@Component
public class SmsService {
    private static Map<String, String> vcMap = new HashMap<>();
    public String genVerifyCode(String phoneNumber) {
        String vc = "123456";
        vcMap.put(phoneNumber, vc);
        return vc;
    }
    public boolean checkVerifyCode(String phoneNumber, String verifyCode) {
        String vc = vcMap.get(phoneNumber);
        if (vc == null) {
            return false;
        } else {
            if (vc.equals(verifyCode)) {
                vcMap.remove(phoneNumber);
                return true;
            } else {
                return false;
            }
        }
    }
}