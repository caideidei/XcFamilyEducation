package com.example.familyeducation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class FamilyEducationApplicationTests {


    @Test
    public void testRest() {
        String url = "http://192.168.111.39/prod-api/api/CaiXinSAEA/queryData";

        String enAKey = "VMvAMcTolvUysBIIfytAdVHZkDWEbq36hdmzmeKn+q5X+ETaAiz4aBYv9g5zT6qD1j0Y6xQpfBARlQkTSyaAatvqoImRvIQvFdBaeETBMnzPe/TbwBoFewAerAi5xSSMZJqGO3YZOo6sxh5TjrJ/8P7qM9HP4EhBxJlHm8tlHexcEUzqSCCf8iqfGygoAIhtANb+RILWy7WvWGtPp5Eo4lwPnxCcRm/XaU1FNBhI3nKY/nEDleNAra5H3SrGh4FqABOpUOGACimcW+Ls+mcR9ObaPVPLI0ZwtQRY3+ZkZuOsLfayNsMNrjA/wd1XY1KPCvt+4GV1+a8Igzdt1+pRlg==";
        String enData = "uKoKD213CP0TlT/FTqbTEZMyUghgXjHd0RIShXAx+Oq8McFYnVi8HAj2+DQKJ91NCMRyqrGIeTXlFQ33waOBqhqfhpMh0h9veytlTSbDRB4=";

        Map<String, Object> param = new HashMap<>();
        param.put("En_AKey", enAKey);
        param.put("En_Data", enData);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(param, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        System.out.println("Response: " + response.getBody());
    }

}
