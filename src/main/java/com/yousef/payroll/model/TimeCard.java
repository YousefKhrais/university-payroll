package com.yousef.payroll.model;

import com.yousef.payroll.model.users.PartTimeAcademic;

import javax.persistence.*;
import java.util.Date;

@Entity
public class TimeCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int hoursCount;
    private Date date;
    private Date createDate = new Date();

    @ManyToOne
    private PartTimeAcademic academic;

    public TimeCard() {
    }

    public TimeCard(int hoursCount, Date date) {
        this.hoursCount = hoursCount;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getHoursCount() {
        return hoursCount;
    }

    public void setHoursCount(int hoursCount) {
        this.hoursCount = hoursCount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public PartTimeAcademic getAcademic() {
        return academic;
    }

    public void setAcademic(PartTimeAcademic academic) {
        this.academic = academic;
    }

    @Override
    public String toString() {
        return "TimeCard{" +
                "id=" + id +
                ", hoursCount=" + hoursCount +
                ", date=" + date +
                ", createDate=" + createDate +
                '}';
    }
}
