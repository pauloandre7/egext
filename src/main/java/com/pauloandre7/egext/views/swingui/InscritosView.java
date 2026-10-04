package com.pauloandre7.egext.views;

import com.pauloandre7.egext.models.InscritoRecord;
import com.pauloandre7.egext.presenters.IInscritoPresenter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class InscritosView extends JFrame implements IInscritoView {

    private IInscritoPresenter presenter;
    private int idSelecionado = -1;

    private JTextField txtNome;
    private JTextField txtEmail;
    private JTextField txtCelular;

    private JButton btnCriar;
    private JButton btnAtualizar;
    private JButton btnExcluir;
    private JButton btnLimpar;

    private JTable tabelaInscritos;
    private DefaultTableModel tableModel;

    public InscritosView() {
        super("Egext - Gestão de Inscritos");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel panelTop = new JPanel(new BorderLayout(5, 5));
        panelTop.setBorder(new TitledBorder("Cadastro e Edição de Inscritos"));

        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNome = new JTextField(20);
        txtEmail = new JTextField(20);
        txtCelular = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        panelCampos.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        panelCampos.add(txtNome, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.0;
        panelCampos.add(new JLabel("E-mail:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0;
        panelCampos.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        panelCampos.add(new JLabel("Celular:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        panelCampos.add(txtCelular, gbc);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnLimpar = new JButton("Limpar");
        btnCriar = new JButton("Cadastrar");
        btnAtualizar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");

        btnLimpar.addActionListener(e -> limparCampos());

        btnCriar.addActionListener(e -> {
            if (presenter != null) {
                presenter.registrarInscrito();
            }
        });

        btnAtualizar.addActionListener(e -> {
            if (presenter != null && idSelecionado != -1) {
                presenter.modificarInscrito(idSelecionado);
            } else if (idSelecionado == -1) {
                exibirMensagemErro("Selecione um inscrito na tabela para atualizar.");
            }
        });

        btnExcluir.addActionListener(e -> {
            if (presenter != null && idSelecionado != -1) {
                presenter.excluirInscrito(idSelecionado);
                limparCampos();
            } else if (idSelecionado == -1) {
                exibirMensagemErro("Selecione um inscrito na tabela para excluir.");
            }
        });

        panelBotoes.add(btnLimpar);
        panelBotoes.add(btnCriar);
        panelBotoes.add(btnAtualizar);
        panelBotoes.add(btnExcluir);

        panelTop.add(panelCampos, BorderLayout.CENTER);
        panelTop.add(panelBotoes, BorderLayout.SOUTH);

        JPanel panelBottom = new JPanel(new BorderLayout());
        panelBottom.setBorder(new TitledBorder("Inscritos Cadastrados"));

        String[] colunas = {"ID", "Nome", "E-mail", "Celular"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaInscritos = new JTable(tableModel);
        tabelaInscritos.setFillsViewportHeight(true);
        tabelaInscritos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tabelaInscritos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabelaInscritos.getSelectedRow();
                if (row >= 0) {
                    idSelecionado = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    txtNome.setText(tableModel.getValueAt(row, 1).toString());
                    txtEmail.setText(tableModel.getValueAt(row, 2).toString());
                    txtCelular.setText(tableModel.getValueAt(row, 3).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelaInscritos);
        panelBottom.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(panelTop, BorderLayout.NORTH);
        mainPanel.add(panelBottom, BorderLayout.CENTER);

        add(mainPanel);
    }

    public void setPresenter(IInscritoPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public String getNomeInput() {
        return txtNome.getText().trim();
    }

    @Override
    public String getEmailInput() {
        return txtEmail.getText().trim();
    }

    @Override
    public String getCelularInput() {
        return txtCelular.getText().trim();
    }

    @Override
    public void listarInscritos(List<InscritoRecord> inscritos) {
        tableModel.setRowCount(0);
        if (inscritos != null) {
            for (InscritoRecord i : inscritos) {
                if (i != null) {
                    Object[] row = {
                        i.id(),
                        i.nome(),
                        i.email(),
                        i.celular()
                    };
                    tableModel.addRow(row);
                }
            }
        }
    }

    @Override
    public void exibirMensagem(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void exibirMensagemErro(String erro) {
        JOptionPane.showMessageDialog(this, erro, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void limparCampos() {
        idSelecionado = -1;
        txtNome.setText("");
        txtEmail.setText("");
        txtCelular.setText("");
        tabelaInscritos.clearSelection();
        txtNome.requestFocus();
    }
}
