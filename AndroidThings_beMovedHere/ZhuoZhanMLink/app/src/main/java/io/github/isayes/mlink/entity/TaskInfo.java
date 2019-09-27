package io.github.isayes.mlink.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Author: HF
 * Date:   2016-05-06
 * Description:
 */

public class TaskInfo extends BmobObject {
    private BmobFile appLogoPic;
    private String appName;
    private String companyName;
    private String taskTodo;
    private String appDescription;

    public BmobFile getAppLogoPic() {
        return appLogoPic;
    }

    public void setAppLogoPic(BmobFile appLogoPic) {
        this.appLogoPic = appLogoPic;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTaskTodo() {
        return taskTodo;
    }

    public void setTaskTodo(String taskTodo) {
        this.taskTodo = taskTodo;
    }

    public String getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }
}
