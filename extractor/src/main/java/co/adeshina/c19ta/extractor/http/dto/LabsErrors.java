package co.adeshina.c19ta.extractor.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LabsErrors {

    private List<Error> errors;

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Error {

        private String type;
        private String detail;
        private String title;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        @Override
        public String toString() {
            return String.format("%nType: %s%nDetail: %s%nTitle: %s%n", type, detail, title);
        }
    }
}
