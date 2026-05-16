package com.codingshuttle.youtube.hospitalManagement.dto;

import com.codingshuttle.youtube.hospitalManagement.entity.type.BloodGroupType;
import java.util.Objects;

// @Data ❌ (manual implementation below)
// @AllArgsConstructor ❌ (manual constructor below)
// @NoArgsConstructor ❌ (manual constructor below)
// @ToString ❌ (manual method below)

public class BloodGroupCountResponseEntity {

    private BloodGroupType bloodGroupType;
    private Long count;

    // ✅ NoArgsConstructor (Lombok @NoArgsConstructor ka manual version)
    public BloodGroupCountResponseEntity() {
    }

    // ✅ AllArgsConstructor (Lombok @AllArgsConstructor ka manual version)
    public BloodGroupCountResponseEntity(BloodGroupType bloodGroupType, Long count) {
        this.bloodGroupType = bloodGroupType;
        this.count = count;
    }

    // ✅ Getter (Lombok @Getter ka manual version)
    public BloodGroupType getBloodGroupType() {
        return bloodGroupType;
    }

    // ✅ Setter (Lombok @Setter ka manual version)
    public void setBloodGroupType(BloodGroupType bloodGroupType) {
        this.bloodGroupType = bloodGroupType;
    }

    // ✅ Getter
    public Long getCount() {
        return count;
    }

    // ✅ Setter
    public void setCount(Long count) {
        this.count = count;
    }

    // ✅ toString (Lombok @ToString ka manual version)
    @Override
    public String toString() {
        return "BloodGroupCountResponseEntity{" +
                "bloodGroupType=" + bloodGroupType +
                ", count=" + count +
                '}';
    }

    // ✅ equals (Lombok @EqualsAndHashCode ka part)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // same reference
        if (o == null || getClass() != o.getClass()) return false; // null ya different class
        BloodGroupCountResponseEntity that = (BloodGroupCountResponseEntity) o;
        return Objects.equals(bloodGroupType, that.bloodGroupType) &&
                Objects.equals(count, that.count); // field comparison
    }

    // ✅ hashCode (Lombok @EqualsAndHashCode ka part)
    @Override
    public int hashCode() {
        return Objects.hash(bloodGroupType, count); // unique hash generate
    }
}