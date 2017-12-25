package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By Mahamdi Amine on 12/25/17
 */
public class GameHistoryPanel extends JPanel {

    private final DataModel model;
    private final JScrollPane scrollPane;
    private final Dimension SCROLL_PANE_DIMENSION = new Dimension(100, 400);

    public GameHistoryPanel() {
        this.setLayout(new BorderLayout());
        this.model = new DataModel();
        final JTable table = new JTable(model);
        table.setRowHeight(50);
        this.scrollPane = new JScrollPane(table);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.setPreferredSize(SCROLL_PANE_DIMENSION);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }

    void redo(final Board board, final Table.MoveLog moveHistory) {
        int currentRow = 0;
        this.model.clear();
        for (final Move move : moveHistory.getMoves()) {
            String moveText = move.toString();
            if (move.getMovedPiece().getPieceAlliance().isWhite())
                this.model.setValueAt(moveText, currentRow, 0);
            else {
                this.model.setValueAt(moveText, currentRow, 1);
                currentRow++;
            }
        }
        if (moveHistory.getMoves().size() > 0) {
            final Move lastmove = moveHistory.getMoves().get(moveHistory.getMoves().size() - 1);
            String movetext = lastmove.toString();
            if (lastmove.getMovedPiece().getPieceAlliance().isWhite()) {
                this.model.setValueAt(movetext + calculateCheckAndCheckMate(board), currentRow, 0);
            } else if (lastmove.getMovedPiece().getPieceAlliance().isBlack()) {
                this.model.setValueAt(movetext + calculateCheckAndCheckMate(board), currentRow - 1, 1);
            }
        }

        final JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());

    }

    private String calculateCheckAndCheckMate(final Board board) {
        if (board.getCurrentPlayer().isInCheckMate()) return "#";
        else if (board.getCurrentPlayer().isInCheck()) return "+";
        else return "";
    }


    private static class DataModel extends DefaultTableModel {
        private final List<Row> values;
        private static String[] SIDES = {"White","Black"};

        DataModel() {
            this.values = new ArrayList<>();
        }

        public void clear() {
            this.values.clear();
            setRowCount(0);
        }

        @Override
        public int getRowCount() {
            if (this.values == null) return 0;
            else return this.values.size();
        }

        @Override
        public int getColumnCount() {
            return SIDES.length;
        }

        @Override
        public Object getValueAt(int row, int column) {
            final Row currentRow = this.values.get(row);
            if (column == 0) return currentRow.getWhiteMove();
            else return currentRow.getBlackMove();
        }

        @Override
        public void setValueAt(final Object o, int row, int column) {
            final Row currentRow;
            if (this.values.size() <= row) {
                currentRow = new Row();
                this.values.add(currentRow);
            } else currentRow = this.values.get(row);
            if (column == 0) {
                currentRow.setWhiteMove((String) o);
                fireTableRowsInserted(row, row);
            } else {
                currentRow.setBlackMove((String) o);
                fireTableCellUpdated(row, column);
            }
        }

        @Override
        public Class<?> getColumnClass(final int column) {
            return Move.class;
        }

        @Override
        public String getColumnName(final int column) {
            return SIDES[column];
        }
    }


    private static class Row {

        Row() {
        }

        private String whiteMove;
        private String blackMove;

        public String getWhiteMove() {
            return whiteMove;
        }

        public String getBlackMove() {
            return blackMove;
        }

        public void setWhiteMove(String move) {
            this.whiteMove = move;
        }

        public void setBlackMove(String move) {
            this.blackMove = move;
        }
    }
}
