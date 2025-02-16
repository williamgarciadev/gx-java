package com.example.processserver.service;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import org.springframework.stereotype.Service;

@Service
public class CpuMonitorService {

    private static final double MAX_CPU_LOAD = 0.9; // Umbral del 80% de uso de CPU

    // Método para verificar la carga actual de la CPU
    public boolean isCpuAvailable() {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double systemCpuLoad = osBean.getSystemCpuLoad(); // Carga de la CPU

        return systemCpuLoad < MAX_CPU_LOAD;
    }
}
