package com.pedidos.view;

import com.pedidos.client.PedidoClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PedidoApp extends JFrame {

    private final JTextField produtoField = new JTextField(20);
    private final JTextField quantidadeField = new JTextField(5);
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID", "Status"}, 0);

    private final Map<UUID, String> pedidosPendentes = new ConcurrentHashMap<>();
    private final PedidoClient client = new PedidoClient();

    public PedidoApp() {
        super("Pedidos - Swing");

        var inputPanel = new JPanel();
        inputPanel.add(new JLabel("Produto:"));
        inputPanel.add(produtoField);
        inputPanel.add(new JLabel("Quantidade:"));
        inputPanel.add(quantidadeField);
        JButton enviarButton = new JButton("Enviar Pedido");
        inputPanel.add(enviarButton);

        var statusTable = new JTable(tableModel);
        var scrollPane = new JScrollPane(statusTable);
        statusTable.setEnabled(false);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        enviarButton.addActionListener(e -> enviarPedido());

        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        iniciarPolling();
    }

    private void enviarPedido() {
        var produto = produtoField.getText().trim();
        var quantidadeStr = quantidadeField.getText().trim();

        try {
            if(!this.validaCampos(produto, quantidadeStr)) return;
            var quantidade = Integer.parseInt(quantidadeStr);
            var id = client.enviarPedido(produto, quantidade);
            pedidosPendentes.put(id, "AGUARDANDO PROCESSO");

            SwingUtilities.invokeLater(() ->
                tableModel.addRow(new Object[]{id.toString(), "ENVIADO, AGUARDANDO PROCESSO"})
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao enviar pedido.");
        }
    }

    private boolean validaCampos(String produto, String quantidadeStr) {
        var isValid = true;
        if(produto.isBlank()) {
            isValid = false;
            JOptionPane.showMessageDialog(this, "O nome do produto deve ser preenchido.");
        } else if(quantidadeStr.isBlank()) {
            isValid = false;
            JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que zero.");
        }
        return isValid;
    }

    private void iniciarPolling() {
        var timer = new Timer(3000, e -> {
            for (UUID id : new ArrayList<>(pedidosPendentes.keySet())) {
                String status = client.consultarStatus(id);
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