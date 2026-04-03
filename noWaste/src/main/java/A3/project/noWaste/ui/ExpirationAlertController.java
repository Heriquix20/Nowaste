package A3.project.noWaste.ui;

import A3.project.noWaste.dto.ExpirationAlertDTO;
import A3.project.noWaste.service.ExpirationAlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
public class ExpirationAlertController {

    private final ExpirationAlertService service;

    public ExpirationAlertController(ExpirationAlertService service) {
        this.service = service;
    }

    @GetMapping("/alerts/expiring")
    public ResponseEntity<List<ExpirationAlertDTO>> findExpiringBatches() {
        return ResponseEntity.ok(service.findExpiringBatches());
    }
}
