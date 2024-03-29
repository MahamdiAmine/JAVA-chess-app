package com.chess.gui;

import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.google.common.primitives.Ints;

import static com.chess.engine.board.Move.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created By Mahamdi Amine on 12/25/17
 */
public class TakenPiecesPanel extends JPanel {
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
    private static final Color PANEL_COLOR = Color.decode("0xFDFE6");
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(60, 80);
    private final JPanel northPanel;
    private final JPanel southPanel;

    private final String imagePath = "/Scripts/chess-Is-My-Life/Images/pieces/";
    private final String imageExtension = ".png";

    public TakenPiecesPanel() {
        super(new BorderLayout());
        setBackground(PANEL_COLOR);
        setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(8, 2));
        this.southPanel = new JPanel(new GridLayout(8, 2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        this.add(this.northPanel, BorderLayout.NORTH);
        this.add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_DIMENSION);

    }

    public void redo(final Table.MoveLog moveLog) {
        southPanel.removeAll();
        northPanel.removeAll();
        final List<Piece> whiteTakenPieces = new ArrayList<>();
        final List<Piece> blackTakenPieces = new ArrayList<>();
        for (final Move move : moveLog.getMoves()) {
            if (move.isAttack()) {
                final Piece takenPiece = move.getAttackedPiece();
                if (takenPiece.getPieceAlliance().isWhite()) {
                    whiteTakenPieces.add(takenPiece);
                } else blackTakenPieces.add(takenPiece);
            }
        }
        Collections.sort(whiteTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece p1, Piece p2) {
                return Ints.compare(p1.getPieceValue(), p2.getPieceValue());
            }
        });
        Collections.sort(blackTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece p1, Piece p2) {
                return Ints.compare(p1.getPieceValue(), p2.getPieceValue());
            }
        });
        for (final Piece takenPieces : whiteTakenPieces) {

            String fullPath = imagePath + takenPieces.getPieceAlliance().toString().substring(0, 1)
                    + takenPieces.toString() + imageExtension;
            try {
                final BufferedImage image = ImageIO.read(new File(fullPath));
                final ImageIcon imageIcon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(imageIcon.getImage().getScaledInstance(
                        imageIcon.getIconWidth() - 15, imageIcon.getIconWidth() - 15, Image.SCALE_SMOOTH)));
                this.southPanel.add(imageLabel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (final Piece takenPieces : blackTakenPieces) {

            String fullPath = imagePath + takenPieces.getPieceAlliance().toString().substring(0, 1)
                    + takenPieces.toString() + imageExtension;
            try {
                final BufferedImage image = ImageIO.read(new File(fullPath));
                final ImageIcon imageIcon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(imageIcon.getImage().getScaledInstance(
                        imageIcon.getIconWidth() - 15, imageIcon.getIconWidth() - 15, Image.SCALE_SMOOTH)));
                this.northPanel.add(imageLabel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        validate();
    }
}
