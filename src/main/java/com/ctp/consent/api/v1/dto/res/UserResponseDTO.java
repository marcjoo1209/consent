package com.ctp.consent.api.v1.dto.res;

import com.ctp.consent.api.v1.dto.BaseDTO;
import lombok.*;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO extends BaseDTO {
    
    private Long id;
    private String username;
    private String name;
    private String email;
    private String phoneNumber;
    private String role;
    private Boolean active;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public String getRoleBadgeClass() {
        if ("SUPER_ADMIN".equals(role)) {
            return "bg-red-100 text-red-800";
        } else if ("ADMIN".equals(role)) {
            return "bg-blue-100 text-blue-800";
        }
        return "bg-gray-100 text-gray-800";
    }
    
    public String getRoleDisplayName() {
        if ("SUPER_ADMIN".equals(role)) {
            return "최고 관리자";
        } else if ("ADMIN".equals(role)) {
            return "관리자";
        }
        return role;
    }
    
    public String getStatusBadgeClass() {
        return active ? "bg-green-100 text-green-800" : "bg-gray-100 text-gray-800";
    }
    
    public String getStatusText() {
        return active ? "활성" : "비활성";
    }
}