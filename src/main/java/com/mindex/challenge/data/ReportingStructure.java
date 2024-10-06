package com.mindex.challenge.data;

public class ReportingStructure {

    private final Employee employee;
    private int numberOfReports;

    public ReportingStructure(final Employee employee, final int numberOfReports) {
        this.employee = employee;
        this.numberOfReports = numberOfReports;
    }
    public Employee getEmployee() {
        return employee;
    }

    public int getNumberOfReports() {
        return numberOfReports;
    }

    public void setNumberOfReports(final int numberOfReports) {
        this.numberOfReports = numberOfReports;
    }
}
