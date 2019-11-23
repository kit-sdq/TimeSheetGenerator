package output;

import data.FullDocumentation;

enum DocumentPlaceholder implements IPlaceholder {

    YEAR("<year>"),
    MONTH("<month>"),
    EMPLOYEE_NAME("<employeeName>"),
    EMPLOYEE_ID("<employeeNumber>"),
    GFUB("<gfub>"),
    DEPARTMENT("<department>"),
    MAX_HOURS("<maxHours>"),
    WAGE("<wage>"),
    VACATION("<vacation>"),
    HOURS_SUM("<sum>"),
    TRANSFER_PRED("<carryFrom>"),
    TRANSFER_SUCC("<carryTo>");
    
    private final String placeholder;
    
    private DocumentPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }
    
    @Override
    public String getPlaceholder() {
        return this.placeholder;
    }
    
    /**
     * Gets a substitute associated with a placeholder from a FullDocumentation.
     * 
     * @param doc - Document to get the substitutes from
     * @return The substitute element parsed to a string
     */
    public String getSubstitute(FullDocumentation doc) {
        String substitute;
        switch (this) {
            case YEAR:
                substitute = Integer.toString(doc.getYear());
                break;
            case MONTH:
                substitute = Integer.toString(doc.getMonth().getValue());
                break;
            case EMPLOYEE_NAME:
                substitute = doc.getEmployee().getName();
                break;
            case EMPLOYEE_ID:
                substitute = Integer.toString(doc.getEmployee().getId());
                break;
            case GFUB:
                substitute = doc.getProfession().getWorkingArea().toString();
                break;
            case DEPARTMENT:
                substitute = doc.getProfession().getDepartmentName();
                break;
            case MAX_HOURS:
                substitute = Integer.toString(doc.getProfession().getMaxWorkingHours());
                break;
            case WAGE:
                substitute = Double.toString(doc.getProfession().getWage());
                break;
            case VACATION:
                substitute = doc.getVacation().toString();
                break;
            case HOURS_SUM:
                substitute = doc.getTotalWorkTime().toString();
                break;
            case TRANSFER_PRED:
                substitute = doc.getPredTranfer().toString();
                break;
            case TRANSFER_SUCC:
                substitute = doc.getSuccTransfer().toString();
                break;
            default:
                substitute = null;
                break;
        }
        return substitute;
    }
}
