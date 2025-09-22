package com.pedidos.view;

import com.pedidos.client.PedidoClient;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PedidoApp extends JFrame {

    private final JTextField produtoField = new JTextField(25);
    private final JTextField quantidadeField = new JTextField(5);
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID", "Status"}, 0);
    private final JTable statusTable = new JTable(tableModel);
    private final Map<UUID, String> pedidosPendentes = new ConcurrentHashMap<>();
    private final PedidoClient client = new PedidoClient();

    public PedidoApp() {
        super("Sistema de Pedidos");

        setLayout(new BorderLayout(10, 10));
        add(criarPainelSuperior(), BorderLayout.NORTH);
        add(new JScrollPane(statusTable), BorderLayout.CENTER);

        configurarTabela();

        setSize(750, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        iniciarPolling();
    }

    private JPanel criarPainelSuperior() {
        var panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        var produtoLabel = new JLabel("Produto:");
        var quantidadeLabel = new JLabel("Quantidade:");
        var enviarButton = new JButton("Enviar Pedido");

        produtoLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        quantidadeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        enviarButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        panel.add(produtoLabel);
        panel.add(produtoField);
        panel.add(quantidadeLabel);
        panel.add(quantidadeField);
        panel.add(enviarButton);

        enviarButton.addActionListener(e -> enviarPedido());

        return panel;
    }

    private void configurarTabela() {
        statusTable.setRowHeight(28);
        statusTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        statusTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        statusTable.setSelectionBackground(new Color(220, 240, 255));
        statusTable.setEnabled(false);

        statusTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                var component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                var status = value.toString();

                if ("SUCESSO".equals(status)) {
                    component.setBackground(new Color(200, 255, 200));
                } else if ("FALHA".equals(status)) {
                    component.setBackground(new Color(255, 200, 200));
                } else {
                    component.setBackground(Color.WHITE);
                }

                return component;
            }
        });
    }

    private void enviarPedido() {
        var produto = produtoField.getText().trim();
        var quantidadeStr = quantidadeField.getText().trim();

        try {
            if (!validaCampos(produto, quantidadeStr)) return;

            var quantidade = Integer.parseInt(quantidadeStr);
            var id = client.enviarPedido(produto, quantidade);
            pedidosPendentes.put(id, "AGUARDANDO PROCESSO");

            SwingUtilities.invokeLater(() ->
                    tableModel.addRow(new Object[]{id.toString(), "ENVIADO, AGUARDANDO PROCESSO"})
            );

            produtoField.setText("");
            quantidadeField.setText("");
            produtoField.requestFocusInWindow();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao enviar pedido.");
        }
    }

    private boolean validaCampos(String produto, String quantidadeStr) {
        if (produto.isBlank()) {
            JOptionPane.showMessageDialog(this, "O nome do produto deve ser preenchido.");
            return false;
        }

        if (quantidadeStr.isBlank()) {
            JOptionPane.showMessageDialog(this, "A quantidade deve ser informada.");
            return false;
        }

        try {
            int quantidade = Integer.parseInt(quantidadeStr);
            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que zero.");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "A quantidade deve ser um número válido.");
            return false;
        }

        return true;
    }

    private void iniciarPolling() {
        var timer = new Timer(3000, e -> {
            for (var id : new ArrayList<>(pedidosPendentes.keySet())) {
                var status = client.consultarStatus(id);
                if ("SUCESSO".equals(status) || "FALHA".equals(status)) {
                    pedidosPendentes.remove(id);
                    atualizarStatusNaTabela(id, status);
                }
            }
        });
        timer.start();
    }

    private void atualizarStatusNaTabela(UUID id, String novoStatus) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(id.toString())) {
                    tableModel.setValueAt(novoStatus, i, 1);
                    break;
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PedidoApp::new);
    }
}