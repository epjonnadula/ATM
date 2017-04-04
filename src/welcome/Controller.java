package welcome;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.*;

public class Controller {

    @FXML private TextField userid,userpin,withdraw_amount,deposit_amount,newpin,
            confirmnewpin,transferuserid,transferamount,add_amount;
    @FXML private AnchorPane welcomePane,menuPane,withdrawPane,errorPane,transscuessPane,depositPane,
            balanceenquiryPane,pinchangePane,transferfundsPane,adminmenuPane,addamountPane;

    public static String id="";

    public void login(ActionEvent event) throws Exception {
        id=userid.getText();

        Class.forName("com.mysql.jdbc.Driver");
        Connection con = null;
        con = DriverManager.getConnection("jdbc:mysql://localhost/test","root", "");
        Statement st=con.createStatement();
        String q="SELECT COUNT(userid) as count FROM account WHERE userid="+"'"+userid.getText()+"' AND userpin="+"'"+userpin.getText()+"'";
        ResultSet rs=st.executeQuery(q);
        rs.next();
        if (rs.getInt(1)!=0)
        {
            AnchorPane pane=FXMLLoader.load(getClass().getResource("menu.fxml"));
            welcomePane.getChildren().setAll(pane);
        }
        else if (userid.getText().equals("admin")&&userid.getText().equals("admin"))
        {
            AnchorPane pane=FXMLLoader.load(getClass().getResource("adminmenu.fxml"));
            welcomePane.getChildren().setAll(pane);
        }
        else
        {
            AnchorPane pane=FXMLLoader.load(getClass().getResource("error.fxml"));
            Label n= (Label) pane.lookup("#error_lable");
            Button b=(Button)pane.lookup("#error");
            n.setText("Invalid Credentials");
            b.setText("Try Again");
            welcomePane.getChildren().setAll(pane);
        }

    }

    public void withdraw(ActionEvent event) throws IOException {
        AnchorPane pane=FXMLLoader.load(getClass().getResource("withdraw.fxml"));
        menuPane.getChildren().setAll(pane);
    }
    public void deposit(ActionEvent event) throws IOException {
        AnchorPane pane=FXMLLoader.load(getClass().getResource("deposit.fxml"));
        menuPane.getChildren().setAll(pane);
    }

    public void balanceenquiry(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = null;
        con = DriverManager.getConnection("jdbc:mysql://localhost/test","root", "");
        Statement st=con.createStatement();
        String query="Select amount from account where userid="+"'"+id+"'";
        ResultSet rs=st.executeQuery(query);
        rs.next();
        Double amount=Double.parseDouble(rs.getString(1));

        AnchorPane pane=FXMLLoader.load(getClass().getResource("balanceenquiry.fxml"));
        Label n= (Label) pane.lookup("#balanceenquiry_amount");
        n.setText(String.valueOf(amount));
        menuPane.getChildren().setAll(pane);

    }
    public void pinchange(ActionEvent event) throws IOException {
        AnchorPane pane=FXMLLoader.load(getClass().getResource("pinchange.fxml"));
        menuPane.getChildren().setAll(pane);
    }
    public void transferfunds(ActionEvent event) throws IOException {
        AnchorPane pane=FXMLLoader.load(getClass().getResource("transferfunds.fxml"));
        menuPane.getChildren().setAll(pane);
    }
    public void addcash(ActionEvent event) throws IOException {
        AnchorPane pane=FXMLLoader.load(getClass().getResource("addamount.fxml"));
        adminmenuPane.getChildren().setAll(pane);
    }
    public void withdrawAmount(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = null;
        con = DriverManager.getConnection("jdbc:mysql://localhost/test","root", "");
        Statement st=con.createStatement();
        String query="Select amount from account where userid="+"'"+id+"'";
        ResultSet rs=st.executeQuery(query);
        rs.next();
        Double amount=Double.parseDouble(rs.getString(1));
        if (amount>Double.parseDouble(withdraw_amount.getText()))
        {
            String query1="Select max_amount from admin where 1";
            ResultSet rs1=st.executeQuery(query1);
            rs1.next();
            Double max_amount=Double.parseDouble(rs1.getString(1));
            if (max_amount>=Double.parseDouble(withdraw_amount.getText()))
            {
                amount=amount-Double.parseDouble(withdraw_amount.getText());
                max_amount=max_amount-Double.parseDouble(withdraw_amount.getText());
                String q1="UPDATE admin SET max_amount = "+"'"+amount+"' where 1";
                st.execute(q1);
                String q="UPDATE account SET amount = "+"'"+amount+"' where userid="+"'"+id+"'";
                st.execute(q);
                AnchorPane pane=FXMLLoader.load(getClass().getResource("transscuess.fxml"));
                withdrawPane.getChildren().setAll(pane);
            }
            else
            {
                AnchorPane pane=FXMLLoader.load(getClass().getResource("error.fxml"));
                Label n= (Label) pane.lookup("#error_lable");
                n.setText("Sufficient Cash not available to dispense");
                withdrawPane.getChildren().setAll(pane);
            }


        }
        else
        {
            AnchorPane pane=FXMLLoader.load(getClass().getResource("error.fxml"));
            Label n= (Label) pane.lookup("#error_lable");
            n.setText("No Sufficient Funds");
            withdrawPane.getChildren().setAll(pane);
        }


    }

    public void depositAmount(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = null;
        con = DriverManager.getConnection("jdbc:mysql://localhost/test","root", "");
        Statement st=con.createStatement();
        String query="Select amount from account where userid="+"'"+id+"'";
        ResultSet rs=st.executeQuery(query);
        rs.next();
        Double amount=Double.parseDouble(rs.getString(1))+Double.parseDouble(deposit_amount.getText());
        String q="UPDATE account SET amount = "+"'"+amount+"' where userid="+"'"+id+"'";
        st.execute(q);


        String query1="Select max_amount from admin where 1";
        ResultSet rs1=st.executeQuery(query1);
        rs1.next();
        Double max_amount=Double.parseDouble(rs1.getString(1))+Double.parseDouble(deposit_amount.getText());
        String q2="UPDATE admin SET max_amount = "+"'"+max_amount+"' where 1";
        st.execute(q2);

        AnchorPane pane=FXMLLoader.load(getClass().getResource("transscuess.fxml"));
        depositPane.getChildren().setAll(pane);

    }

    public void balanceenquiryamount(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        AnchorPane pane=FXMLLoader.load(getClass().getResource("transscuess.fxml"));
        balanceenquiryPane.getChildren().setAll(pane);
    }


    public void pinchangeTrans(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = null;
        con = DriverManager.getConnection("jdbc:mysql://localhost/test","root", "");
        Statement st=con.createStatement();
        if (newpin.getText().equals(confirmnewpin.getText()))
        {
            String q="UPDATE account SET userpin = "+"'"+newpin.getText()+"' where userid="+"'"+id+"'";
            st.execute(q);
            AnchorPane pane=FXMLLoader.load(getClass().getResource("transscuess.fxml"));
            pinchangePane.getChildren().setAll(pane);
        }
        else
        {
            AnchorPane pane=FXMLLoader.load(getClass().getResource("error.fxml"));
            Label n= (Label) pane.lookup("#error_lable");
            n.setText("Enter same PIN for confirmation");
            pinchangePane.getChildren().setAll(pane);
        }
    }

    public void transferfundsTrans(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = null;
        con = DriverManager.getConnection("jdbc:mysql://localhost/test","root", "");
        Statement st=con.createStatement();
        String q1="Select amount from account where userid="+"'"+id+"'";
        ResultSet rs1=st.executeQuery(q1);
        rs1.next();
        Double amount1=Double.parseDouble(rs1.getString(1));

        Double entered=Double.parseDouble(transferamount.getText());

        String q2="SELECT COUNT(userid) as count FROM account WHERE userid="+"'"+transferuserid.getText()+"'";
        ResultSet rs2=st.executeQuery(q2);
        rs2.next();
        if (rs2.getInt(1)!=0)
        {

            if (entered<amount1)
            {
                String q3="select amount from account where userid="+"'"+transferuserid.getText()+"'";
                ResultSet rs3=st.executeQuery(q3);
                rs3.next();
                Double amount2=Double.parseDouble(rs3.getString(1));

                amount1=amount1-entered;
                amount2=amount2+entered;
                String q4="UPDATE account SET amount = "+"'"+amount1+"' where userid="+"'"+id+"'";
                st.execute(q4);
                String q5="UPDATE account SET amount = "+"'"+amount2+"' where userid="+"'"+transferuserid.getText()+"'";
                st.execute(q5);
                AnchorPane pane=FXMLLoader.load(getClass().getResource("transscuess.fxml"));
                transferfundsPane.getChildren().setAll(pane);
            }
            else
            {
                AnchorPane pane=FXMLLoader.load(getClass().getResource("error.fxml"));
                Label n= (Label) pane.lookup("#error_lable");
                n.setText("No Sufficient Funds");
                transferfundsPane.getChildren().setAll(pane);
            }
        }
        else
        {
            AnchorPane pane=FXMLLoader.load(getClass().getResource("error.fxml"));
            Label n= (Label) pane.lookup("#error_lable");
            n.setText("Enter Valid UserID");
            transferfundsPane.getChildren().setAll(pane);
        }

    }

    public void addAmount(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = null;
        con = DriverManager.getConnection("jdbc:mysql://localhost/test","root", "");
        Statement st=con.createStatement();
        String query="Select max_amount from admin where 1";
        ResultSet rs=st.executeQuery(query);
        rs.next();
        Double amount=Double.parseDouble(rs.getString(1))+Double.parseDouble(add_amount.getText());
        String q="UPDATE admin SET max_amount = "+"'"+amount+"' where 1";
        st.execute(q);
        AnchorPane pane=FXMLLoader.load(getClass().getResource("transscuess.fxml"));
        addamountPane.getChildren().setAll(pane);

    }

    public void error(ActionEvent event) throws IOException {
        AnchorPane pane=FXMLLoader.load(getClass().getResource("welcome.fxml"));
        id="";
        errorPane.getChildren().setAll(pane);
    }

    public void transscuess(ActionEvent event) throws IOException {
        AnchorPane pane=FXMLLoader.load(getClass().getResource("welcome.fxml"));
        id="";
        transscuessPane.getChildren().setAll(pane);
    }

}
