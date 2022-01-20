package com.yousef.payroll.model.users;

import com.yousef.payroll.model.TimeCard;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Entity
public class PartTimeAcademic implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Academic academic;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_id")
    private List<TimeCard> timeCards = new ArrayList<>();

    public PartTimeAcademic() {
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
        return 0;
    }

    public List<TimeCard> getTimeCards() {
        return timeCards;
    }

    public void setTimeCards(List<TimeCard> timeCards) {
        this.timeCards = timeCards;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        HashSet<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ACADEMIC"));
        return authorities;
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
        return "PartTimeAcademic{" +
                "id=" + id +
                ", academic=" + academic +
                '}';
    }
}
