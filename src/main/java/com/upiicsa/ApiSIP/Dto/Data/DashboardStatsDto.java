package com.upiicsa.ApiSIP.Dto.Data;

public record DashboardStatsDto(
        int total,
        int registered,
        int docInitial,
        int letterAccep,
        int docFinal
) {
}
