package com.yousef.payroll.model;

import com.yousef.payroll.model.types.LeaveType;

import javax.persistence.*;
import java.util.Date;

@Entity
public class AcademicLeave {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int days;
    private String reason;

    private Date fromDate;
    private Date toDate;
    private Date createdDate = new Date();

    private LeaveType type;

    @ManyToOne
    private Academic academic;

    public AcademicLeave() {
    }

    public AcademicLeave(Date fromDate, Date toDate, String reason) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Academic getAcademic() {
        return academic;
    }

    public void setAcademic(Academic academic) {
        this.academic = academic;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public LeaveType getType() {
        return type;
    }

    public void setType(LeaveType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AcademicLeave{" +
                "id=" + id +
                ", days=" + days +
                ", reason='" + reason + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", createdDate=" + createdDate +
                ", type=" + type +
                '}';
    }
}
