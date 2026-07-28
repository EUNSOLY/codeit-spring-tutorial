package com.demo.demo;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum HttpErrorStatus {
    NOT_FOUND(404, "404 Not Found", "Page does not exist", Level.WARN),
    INTERNAL_SERVER_ERROR(500, "500 Internal Server Error", "Any exception is occurred in application", Level.ERROR);

    int code;
    String title;
    String description;
    Level logLevel;

    public ModelAndView toModelAndView() {
        return this.toModelAndView("error");
    }

    public ModelAndView toModelAndView(String viewTemplate) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(viewTemplate);
        modelAndView.addObject("title", this.title);
        modelAndView.addObject("description", this.description);
        log.makeLoggingEventBuilder(this.logLevel)
                .log(String.format("%s - %s", this.title, this.description));
        return modelAndView;
    }
}
