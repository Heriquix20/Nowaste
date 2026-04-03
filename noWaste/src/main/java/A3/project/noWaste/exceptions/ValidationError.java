package A3.project.noWaste.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ValidationError extends StandardError {

    private List<String> errors = new ArrayList<>();

    public ValidationError(LocalDateTime timestamp, Integer status, String error, String path) {
        super(timestamp, status, error, path);
    }

    public void addError(String message) {
        this.errors.add(message);
    }
}
