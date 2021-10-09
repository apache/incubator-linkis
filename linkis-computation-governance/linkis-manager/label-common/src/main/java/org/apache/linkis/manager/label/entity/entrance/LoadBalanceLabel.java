package org.apache.linkis.manager.label.entity.entrance;

import org.apache.linkis.manager.label.constant.LabelKeyConstant;
import org.apache.linkis.manager.label.entity.Feature;
import org.apache.linkis.manager.label.entity.GenericLabel;
import org.apache.linkis.manager.label.entity.annon.ValueSerialNum;

import java.util.HashMap;

public class LoadBalanceLabel extends GenericLabel implements JobStrategyLabel{

    public LoadBalanceLabel(){
        setLabelKey(LabelKeyConstant.LOAD_BALANCE_KEY);
    }

    @Override
    public Feature getFeature() {
        return Feature.OPTIONAL;
    }

    public Integer getCapacity() {
        if (null == getValue()) {
            return null;
        }
        return Integer.parseInt(getValue().get("capacity"));
    }

    @ValueSerialNum(0)
    public LoadBalanceLabel setCapacity(String capacity) {
        if (null == getValue()) {
            setValue(new HashMap<>());
        }
        getValue().put("capacity", capacity);
        return this;
    }

    public String getGroupId() {
        if (null == getValue()) {
            return null;
        }
        return getValue().get("groupId");
    }

    @ValueSerialNum(1)
    public LoadBalanceLabel setGroupId(String groupId) {
        if (null == getValue()) {
            setValue(new HashMap<>());
        }
        getValue().put("groupId", groupId);
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (LoadBalanceLabel.class.isInstance(other)) {
            if (null != getGroupId()) {
                return getGroupId().equals(((LoadBalanceLabel)other).getGroupId());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
