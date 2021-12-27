package com.tradeTracker.email;

import com.tradeTracker.configuration.Configuration;
import com.tradeTracker.reportContents.Company;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DividendMessageBuilder extends MessageBuilder{

    private final List<Company> listOfCompaniesBase;
    private final List<Company> listOfCompanies;

    public DividendMessageBuilder(List<Company> listOfCompaniesBase, List<Company> listOfCompanies, Configuration configuration) {
        super(configuration);
        this.listOfCompaniesBase = listOfCompaniesBase;
        this.listOfCompanies = listOfCompanies;
    }

    public void sendDividendEmail() {
        this.sendEmail(
                this.getHtmlTable(),
                "Dividends on " + new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis()-24*60*60*1000))
        );
    }

    private String getHtmlTable() {
        String htmlTableHeader = """
                <table style="border-collapse: collapse; width: 100%; height: 72px;" border="1">
                <tbody>
                <tr style="height: 18px;">
                <td style="width: 10%; height: 18px;"><span style="background-color: #ffffff;"><strong>Date</strong></span></td>
                <td style="width: 20%; height: 18px;"><span style="background-color: #ffffff;"><strong>Company</strong></span></td>
                <td style="width: 10%; height: 18px;"><span style="background-color: #ffffff;"><strong>Amount</strong></span></td>
                <td style="width: 10%; height: 18px;"><span style="background-color: #ffffff;"><strong>Taxes</strong></span></td>
                <td style="width: 10%; height: 18px;"><span style="background-color: #ffffff;"><strong>Net</strong></span></td>
                <td style="width: 10%; height: 18px;"><span style="background-color: #ffffff;"><strong>Currency</strong></span></td>
                <td style="width: 10%; height: 18px;"><span style="background-color: #ffffff;"><strong>Rate</strong></span></td>
                </tr>
                """;
        StringBuilder sb = new StringBuilder("<h3>Dividends in EUR:</h3>");
        sb.append(htmlTableHeader);
        listOfCompaniesBase.forEach(company -> sb.append(getTableRow(company)));

        sb.append("""
                </tr>
                </tbody>
                </table>""");
        sb.append("<h3>\n</h3>");
        sb.append("<h3>Dividends in original currency:</h3>");
        sb.append(htmlTableHeader);
        listOfCompanies.forEach(company -> sb.append(getTableRow(company)));
        sb.append("""
                </tr>
                </tbody>
                </table>""");
        return sb.toString();
    }

    private String getTableRow(Company company) {
        DecimalFormat df = new DecimalFormat("0.00");
        return  "<tr style=\"height: 18px;\">\n" +
                "<td style=\"width: 10%; height: 18px;\">"+new SimpleDateFormat("dd/MM/yyyy").format(company.getPaymentDate()) +"</td>\n" +
                "<td style=\"width: 20%; height: 18px;\">"+company.getCompanyName()+"</td>\n" +
                "<td style=\"width: 10%; height: 18px;\">"+company.getAmount()+"</td>\n" +
                "<td style=\"width: 10%; height: 18px;\">"+company.getTax()+ " (" + df.format(-100*company.getTax()/company.getAmount())+"%)</td>\n" +
                "<td style=\"width: 10%; height: 18px;\">"+df.format(Double.valueOf(company.getAmount() + company.getTax()))+"</td>\n" +
                "<td style=\"width: 10%; height: 18px;\">"+company.getCurrency()+"</td>\n" +
                "<td style=\"width: 10%; height: 18px;\">"+company.getRate()+"</td>\n" +
                "</tr>\n";
    }
}
