package org.ui;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.controller.Controller;
import org.model.Cidades;
import org.model.Endereco;
import org.model.Estados;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class frmPrincipal extends JFrame {
    private JPanel panelPrincipal;
    private JButton btnEnderecos;
    private JButton bntCadastros;
    private JTable tbEnderecos;
    private JScrollPane panelScroll;
    private JPanel panelTable;
    private JPanel panelOpcoes;
    private JTextField tfBairro;
    private JTextField tfCidade;
    private JTextField tfCEP;
    private JButton btnCadastrar;
    private JPanel panelCadastro;
    private JComboBox cbUF;
    private JButton buscarButton;
    private JTextField tfRua;
    private JLabel lbRua;
    private JLabel lbBairro;
    private JLabel lbCidade;
    private JLabel lbCEP;
    private JLabel lbUF;
    private JComboBox cbCidade;
    private JButton btnDeletar;
    private JButton bntSalvar;
    private JTextField textField1;

    private DefaultTableModel model;

    public frmPrincipal(){
        super();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(0,0,900,600);
        this.setContentPane(panelPrincipal);

        /*for (Estados estado: Controller.getAllEstados()) {
            cbUF.addItem(estado.getUf());
        }

        for (Cidades cidade: Controller.getAllCidades()) {
            cbCidade.addItem(cidade.getNome());
        }*/

        carregarModel();

        tbEnderecos.getTableHeader().setBackground(Color.decode("#44475a"));
        tbEnderecos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        tbEnderecos.getTableHeader().setForeground(Color.decode("#f8f8f2"));
        tbEnderecos.getTableHeader().setOpaque(true);
        tbEnderecos.setFont(new Font("Arial", Font.PLAIN, 13));

        LineBorder lineBorder = new LineBorder(Color.decode("#ff79c6"),2, true);

        tfBairro.setBorder(lineBorder);
        tfCEP.setBorder(lineBorder);
        tfRua.setBorder(lineBorder);

        panelTable.setOpaque(false);

        btnEnderecos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                panelCadastro.setVisible(false);
                panelTable.setVisible(true);
            }
        });
        bntCadastros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                panelCadastro.setVisible(true);
                panelTable.setVisible(false);
            }
        });
        btnCadastrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                Endereco endereco = new Endereco();

                //endereco.setCEP(tfCEP.getText());
                //endereco.setRua(tfRua.getText());
                //endereco.setBairro(tfBairro.getText());

                //Controller.CadastroEndereco(endereco);

            }
        });
        buscarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JSONObject jsonObject;
                JSONParser parser = new JSONParser();
                HttpClient client = HttpClient.newHttpClient();

                HttpRequest request = HttpRequest.newBuilder()
                        .GET()
                        .header("accept", "application/json")
                        .uri(URI.create("https://viacep.com.br/ws/" + tfCEP.getText() + "/json/"))
                        .build();

                try {
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    jsonObject = (JSONObject) parser.parse(response.body());

                    tfBairro.setText((String) jsonObject.get("bairro"));
                    tfRua.setText((String) jsonObject.get("logradouro"));
                    tfCidade.setText((String) jsonObject.get("localidade"));

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        btnDeletar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                for (int row : tbEnderecos.getSelectedRows()) {
                    System.out.println(model.getValueAt(row,0));
                    //Controller.deleteEndereco((long)model.getValueAt(row,0));
                    model.removeRow(row);
                }
            }
        });
    }

    public void carregarModel() {

        model = (DefaultTableModel) tbEnderecos.getModel();

        model.addColumn("Id");
        model.addColumn("Rua");
        model.addColumn("Numero");
        model.addColumn("Complemento");
        model.addColumn("Bairro");
        model.addColumn("Cidade");
        model.addColumn("CEP");

        /*for (Endereco endereco : Controller.getAllEnderecos()) {
            model.addRow(new Object[]{
                    endereco.getIdEndereco(),
                    endereco.getRua(),
                    endereco.getNumeroCasa(),
                    endereco.getComplemento(),
                    endereco.getBairro(),
                    endereco.getCidade().getNome(),
                    endereco.getCEP()
            });
        }*/

        tbEnderecos.setModel(model);

        TableColumnModel tableColumnModel = tbEnderecos.getColumnModel();
        tableColumnModel.removeColumn(tableColumnModel.getColumn(0));

    }

}
