package com.ctp.consent.api.v1.dto.validation;

import jakarta.validation.groups.Default;

public interface ValidationGroups {
    
    interface Create extends Default {}
    
    interface Update extends Default {}
    
    interface Delete extends Default {}
    
    interface Patch extends Default {}
    
    interface Query extends Default {}
    
    interface Login extends Default {}
    
    interface Register extends Default {}
    
    interface ChangePassword extends Default {}
    
    interface FileUpload extends Default {}
    
    interface ConsentSubmit extends Default {}
    
    interface ConsentApprove extends Default {}
    
    interface ConsentReject extends Default {}
    
    interface AdminAction extends Default {}
    
    interface PublicAction extends Default {}
    
    interface BulkOperation extends Default {}
    
    interface Export extends Default {}
    
    interface Import extends Default {}
}