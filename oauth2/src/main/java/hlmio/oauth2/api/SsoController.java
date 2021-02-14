package hlmio.oauth2.api;

import hlmio.oauth2.conf.security.other.JsonResult;
import hlmio.oauth2.conf.security.other.NoAuth;
import hlmio.oauth2.conf.security.other.ResultCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.security.Principal;


@RestController
public class SsoController {

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Principal> get(final Principal principal) {
        return ResponseEntity.ok(principal);
    }


	@GetMapping("/test1")
	public JsonResult test1() {
		return new JsonResult(ResultCode.SUCCESS);
	}

	@NoAuth
	@GetMapping("/test2")
	public JsonResult test2() {
		return new JsonResult(ResultCode.SUCCESS);
	}


}
