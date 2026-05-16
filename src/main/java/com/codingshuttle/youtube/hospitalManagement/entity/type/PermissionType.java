package com.codingshuttle.youtube.hospitalManagement.entity.type;

public enum PermissionType {

    // 🔥 Enum constants + value pass kar rahe
    PATIENT_READ("patient:read"),
    PATIENT_WRITE("patient:write"),
    APPOINTMENT_READ("appointment:read"),
    APPOINTMENT_WRITE("appointment:write"),
    APPOINTMENT_DELETE("appointment:delete"),
    USER_MANAGE("user:manage"), // admin ke liye
    REPORT_VIEW("report:view");

    // 🔐 actual permission string
    private final String permission;

    // ✅ Constructor (Lombok @RequiredArgsConstructor ka replacement)
    PermissionType(String permission) {
        this.permission = permission;
    }

    // ✅ Getter (Lombok @Getter ka replacement)
    public String getPermission() {
        return permission;
    }
}