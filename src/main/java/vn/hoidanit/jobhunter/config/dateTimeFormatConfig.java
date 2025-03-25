package vn.hoidanit.jobhunter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Config này giúp thiết lập định dạng thời gian mặc định khi nhận dữ liệu ngày giờ từ Req
@Configuration
public class dateTimeFormatConfig implements WebMvcConfigurer {
    @Override
    // Định dạng ngày giờ giúp tự động chuyển đổi dữ liệu ngày giờ từ req thành
    // Local Date or LocalDateTime mà k cần khai báo @DateTimeFormat
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true);
        registrar.registerFormatters(registry);
    }
}
