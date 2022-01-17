package com.yousef.payroll.model.users;

import com.yousef.payroll.model.AcademicLeave;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class FullTimeAcademic implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Academic academic;

    @NotNull(message = "Leave Balance can not be empty")
    @Min(value = 0, message = "Leave Balance can not be less than 0")
    private int leaveBalance;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_id")
    private List<AcademicLeave> academicLeaves = new ArrayList<>();

    public FullTimeAcademic() {
    }

    public FullTimeAcademic(Academic academic) {
        this.academic = academic;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Academic getAcademic() {
        return academic;
    }

    public void setAcademic(Academic academic) {
        this.academic = academic;
    }

    public int getLeaveBalance() {
        return leaveBalance;
    }

    public void setLeaveBalance(int leaveBalance) {
        this.leaveBalance = leaveBalance;
    }

    public List<AcademicLeave> getAcademicLeaves() {
        return academicLeaves;
    }

    public void setAcademicLeaves(List<AcademicLeave> academicLeaves) {
        this.academicLeaves = academicLeaves;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return academic.getPassword();
    }

    @Override
    public String getUsername() {
        return academic.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "FullTimeAcademic{" +
                "id=" + id +
                ", academic=" + academic +
                ", leaveBalance=" + leaveBalance +
                ", academicLeaves=" + academicLeaves +
                '}';
    }
}