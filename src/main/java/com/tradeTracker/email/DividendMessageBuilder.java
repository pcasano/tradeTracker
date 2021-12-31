package com.tradeTracker.email;

import com.tradeTracker.Main;
import com.tradeTracker.configuration.Configuration;
import com.tradeTracker.reportContents.Company;
import com.tradeTracker.reportContents.FlexStatement;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DividendMessageBuilder extends MessageBuilder {

    private final List<Company> listOfCompaniesBase;
    private final List<Company> listOfCompanies;
    private final String emailSubject;

    public DividendMessageBuilder(List<Company> listOfCompaniesBase, List<Company> listOfCompanies, Configuration configuration, FlexStatement flexStatement) {
        super(configuration);
        this.listOfCompaniesBase = listOfCompaniesBase;
        this.listOfCompanies = listOfCompanies;
        if (flexStatement.getFromDate().equals(flexStatement.getToDate())) {
            this.emailSubject = "Dividends on " + sdf.format(flexStatement.getFromDate());
        } else {
            this.emailSubject = "Dividends from " + sdf.format(flexStatement.getFromDate()) + " to " + sdf.format(flexStatement.getToDate());
        }
    }

    public void sendDividendEmail() {
        this.sendEmail(
                this.getHtmlTable(),
                this.emailSubject
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
        StringBuilder sb = new StringBuilder("<h3>Dividends in original currency:</h3>");
        sb.append(htmlTableHeader);
        listOfCompanies.forEach(company -> sb.append(getTableRow(company)));

        sb.append("""
                </tr>
                </tbody>
                </table>""");
        sb.append("<h3>\n</h3>");
        sb.append("<h3>Dividends in EUR: ").append(df.format(listOfCompaniesBase.stream().mapToDouble(Company::getAmount).sum())).append(" euros").append("</h3>");
        sb.append(htmlTableHeader);
        listOfCompaniesBase.forEach(company -> {
            sb.append(getTableRow(company));
            logger.info("dividend added: " + company.getCompanyName() + " | " + company.getAmount() + " " + company.getCurrency());
        });
        sb.append("""
                </tr>
                </tbody>
                </table>""");
        return sb.toString();
    }

    private String getTableRow(Company company) {
        return "<tr style=\"height: 18px;\">\n" +
                "<td style=\"width: 10%; height: 18px;\">" + sdf.format(company.getPaymentDate()) + "</td>\n" +
                "<td style=\"width: 20%; height: 18px;\">" + company.getCompanyName() + "</td>\n" +
                "<td style=\"width: 10%; height: 18px;\">" + df.format(company.getAmount()) + "</td>\n" +
                "<td style=\"width: 10%; height: 18px;\">" + df.format((company.getTax())) + " (" + df.format(-100 * company.getTax() / company.getAmount()) + "%)</td>\n" +
                "<td style=\"width: 10%; height: 18px;\">" + df.format(Double.valueOf(company.getAmount() + company.getTax())) + "</td>\n" +
                "<td style=\"width: 10%; height: 18px;\">" + company.getCurrency() + "</td>\n" +
                "<td style=\"width: 10%; height: 18px;\">" + company.getRate() + "</td>\n" +
                "</tr>\n";
    }
}
