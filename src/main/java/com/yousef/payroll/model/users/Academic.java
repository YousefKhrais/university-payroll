package com.yousef.payroll.model.users;

import com.yousef.payroll.model.Payment;
import com.yousef.payroll.model.types.AcademicType;
import com.yousef.payroll.model.types.Gender;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "academic")
public class Academic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Email is not valid")
    @NotBlank(message = "Email can not be empty")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password can not be empty")
    private String password;

    @NotBlank(message = "First Name can not be empty")
    private String firstName;

    @NotBlank(message = "Last Name can not be empty")
    private String lastName;

    @NotBlank(message = "Department can not be empty")
    private String department;

    @NotBlank(message = "Phone Number can not be empty")
    private String phoneNumber;

    @NotBlank(message = "Address can not be empty")
    private String address;

    private Date joinDate = new Date();

    private AcademicType type;

    private Gender gender;
    private String profilePicLink;
    private String jobTitle;
    private String paymentDetails;
    private boolean isActive;
    private Date birthDate;
    private boolean sendEmailNotification;

    @NotNull(message = "Flat Salary can not be empty")
    @Min(value = 0, message = "Flat Salary can not be less than 0")
    private double flatSalary;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "academic_id")
    private List<Payment> payments = new ArrayList<>();

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

    public String getPassword() {
        return password;
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

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public AcademicType getType() {
        return type;
    }

    public void setType(AcademicType type) {
        this.type = type;
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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isSendEmailNotification() {
        return sendEmailNotification;
    }

    public void setSendEmailNotification(boolean sendEmailNotification) {
        this.sendEmailNotification = sendEmailNotification;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public double getFlatSalary() {
        return flatSalary;
    }

    public void setFlatSalary(double flatSalary) {
        this.flatSalary = flatSalary;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "Academic{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", department='" + department + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", joinDate=" + joinDate +
                ", type=" + type +
                ", gender=" + gender +
                ", profilePicLink='" + profilePicLink + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", paymentDetails='" + paymentDetails + '\'' +
                ", isActive=" + isActive +
                ", birthDate=" + birthDate +
                ", sendEmailNotification=" + sendEmailNotification +
                ", flatSalary=" + flatSalary +
                '}';
    }
}
