package us.kesslern.freshms.util

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

class RestTemplateFactory {

    static RestTemplate build() {
        new RestTemplate().with {
            messageConverters.add(new MappingJackson2HttpMessageConverter())
            it
        }
    }
}
