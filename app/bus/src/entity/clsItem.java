 /*
  *  clsItem.java
  *  Created on Aug 22, 2013
  */

package app.infobus.entity;

import java.util.List;

public class clsItem {
    String num;
    public String getNum() {
        return num;
    }
    public void setNum(String num) {
        this.num = num;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getStart() {
        return start;
    }
    public void setStart(String start) {
        this.start = start;
    }
    public String getBack() {
        return back;
    }
    public void setBack(String back) {
        this.back = back;
    }
    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    String name;
    String start;
    String back;
    String info;
    public List<LocXY> locS;
    public List<LocXY> locB;

}
