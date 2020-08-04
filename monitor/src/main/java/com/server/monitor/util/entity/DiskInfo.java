package com.server.monitor.util.entity;

import com.server.monitor.util.DiskUtil;

import java.math.BigDecimal;

public class DiskInfo {
    BigDecimal totalSpace = BigDecimal.ZERO;
    BigDecimal freeSpace = BigDecimal.ZERO;
    BigDecimal usedSpace = BigDecimal.ZERO;

    public DiskInfo(BigDecimal totalSpace, BigDecimal usedSpace, BigDecimal freeSpace) {
        this.totalSpace = totalSpace;
        this.freeSpace = freeSpace;
        this.usedSpace = usedSpace;
    }

    public BigDecimal getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(BigDecimal totalSpace) {
        this.totalSpace = totalSpace;
    }

    public BigDecimal getFreeSpace() {
        return freeSpace;
    }

    public void setFreeSpace(BigDecimal freeSpace) {
        this.freeSpace = freeSpace;
    }

    public BigDecimal getUsedSpace() {
        return usedSpace;
    }

    public void setUsedSpace(BigDecimal usedSpace) {
        this.usedSpace = usedSpace;
    }

    public BigDecimal getPercentage() {
        return  freeSpace.divide(usedSpace,2,BigDecimal.ROUND_HALF_DOWN).multiply( DiskUtil.hundred );
    }
}
