package com.github.binarywang.demo.wechat.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Analysis {
    @Id
    @GeneratedValue
    private int id;

    private Date refdate;

    private int usersource;

    private int newuser;

    private int canceluser;

    private int netincrease;

    private Date updatetime;

    public Analysis() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getRefdate() {
        return refdate;
    }

    public void setRefdate(Date refdate) {
        this.refdate = refdate;
    }

    public int getUsersource() {
        return usersource;
    }

    public void setUsersource(int usersource) {
        this.usersource = usersource;
    }

    public int getNewuser() {
        return newuser;
    }

    public void setNewuser(int newuser) {
        this.newuser = newuser;
    }

    public int getCanceluser() {
        return canceluser;
    }

    public void setCanceluser(int canceluser) {
        this.canceluser = canceluser;
    }

    public int getNetincrease() {
        return netincrease;
    }

    public void setNetincrease(int netincrease) {
        this.netincrease = netincrease;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "Analysis{" +
                "id=" + id +
                ", refdate=" + refdate +
                ", usersource=" + usersource +
                ", newuser=" + newuser +
                ", canceluser=" + canceluser +
                ", netincrease=" + netincrease +
                '}';
    }
}
