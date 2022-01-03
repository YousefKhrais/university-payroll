package com.yousef.payroll.model;

import com.yousef.payroll.model.types.AcademicType;
import com.yousef.payroll.model.types.Gender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "academic")
public class Academic implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Email is not valid")
    @NotEmpty(message = "Email can not be empty")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "Password can not be empty")
    private String password;

    @NotEmpty(message = "First Name can not be empty")
    private String firstName;

    @NotEmpty(message = "Last Name can not be empty")
    private String lastName;

    @NotEmpty(message = "Department can not be empty")
    private String department;

    @NotEmpty(message = "Phone Number can not be empty")
    private String phoneNumber;

    @NotEmpty(message = "Address can not be empty")
    private String address;

    @NotNull(message = "Flat Salary can not be empty")
    @Min(value = 0, message = "Flat Salary can not be less than 0")
    private double flatSalary;

    @NotNull(message = "Leave Balance can not be empty")
    @Min(value = 0, message = "Leave Balance can not be less than 0")
    private int leaveBalance;

    private String profilePicLink;
    private String jobTitle;
    private String paymentDetails;
    private boolean sendEmailNotification;
    private boolean isActive;
    private AcademicType type;
    private Gender gender;
    private Date birthDate;
    private Date joinDate = new Date();

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "academic_id")
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_id")
    private List<AcademicLeave> academicLeaves = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_id")
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_id")
    private List<TimeCard> timeCards = new ArrayList<>();

    public Academic() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public AcademicType getType() {
        return type;
    }

    public void setType(AcademicType type) {
        this.type = type;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(ArrayList<Payment> payments) {
        this.payments = payments;
    }

    public List<AcademicLeave> getAcademicLeaves() {
        return academicLeaves;
    }

    public void setAcademicLeaves(ArrayList<AcademicLeave> academicLeaves) {
        this.academicLeaves = academicLeaves;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getFlatSalary() {
        return flatSalary;
    }

    public void setFlatSalary(double flatSalary) {
        this.flatSalary = flatSalary;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public int getLeaveBalance() {
        return leaveBalance;
    }

    public void setLeaveBalance(int leaveBalance) {
        this.leaveBalance = leaveBalance;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getProfilePicLink() {
        return profilePicLink;
    }

    public void setProfilePicLink(String profilePicLink) {
        this.profilePicLink = profilePicLink;
    }

    public boolean isSendEmailNotification() {
        return sendEmailNotification;
    }

    public void setSendEmailNotification(boolean sendEmailNotification) {
        this.sendEmailNotification = sendEmailNotification;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<TimeCard> getTimeCards() {
        return timeCards;
    }

    public void setTimeCards(List<TimeCard> timeCards) {
        this.timeCards = timeCards;
    }

    @Override
    public String toString() {
        return "Academic{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", type=" + type +
                ", department='" + department + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", paymentDetails='" + paymentDetails + '\'' +
                ", flatSalary=" + flatSalary +
                ", leaveBalance=" + leaveBalance +
                ", joinDate=" + joinDate +
                ", gender=" + gender +
                ", profilePicLink='" + profilePicLink + '\'' +
                ", sendEmailNotification=" + sendEmailNotification +
                ", jobTitle='" + jobTitle + '\'' +
                ", birthDate=" + birthDate +
                ", isActive=" + isActive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Academic academic = (Academic) o;

        return id != null ? id.equals(academic.id) : academic.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}