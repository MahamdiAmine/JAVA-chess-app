package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.SwingUtilities.*;

/**
 * Created By Mahamdi Amine on 12/23/17
 */
public class Table {
    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
    private final Color LIGHT_TILT_COLOR = Color.decode("#FFFFFF");
    private final Color DARK_TILE_COLOR = Color.decode("#1D3D63");
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board chessboard;
    private BoardDirection boardDirection;
    private static String pieceImagesPath = "/Scripts/chess-Is-My-Life/Images/";
    private static String pieceImagesExtension = ".png";
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;

    public Table() {
        this.chessboard = Board.createStandardBoard();
        this.boardDirection=BoardDirection.NORMAL;
        this.gameFrame = new JFrame("Chess is my life ");
        this.boardPanel = new BoardPanel();
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setLayout(new BorderLayout());
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameFrame.setVisible(true);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN file");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("open up that pgn file");
            }
        });
        fileMenu.add(openPGN);
        final JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("YOU LOOSE");
                System.exit(0);
            }
        });
        fileMenu.add(exit);
        return fileMenu;
    }

    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }


        public void drawBoard(final Board board) {
            removeAll();
            for (final TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
                tilePanel.drawTile(board);
                add(tilePanel);
                validate();
                repaint();
            }
        }
    }

    private class TilePanel extends JPanel {
        private final int tileId;

        TilePanel(final BoardPanel boardPanel, final int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent mouseEvent) {
                    if (isRightMouseButton(mouseEvent)) {
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    } else if (isLeftMouseButton(mouseEvent)) {

                        if (sourceTile == null) {
                            sourceTile = chessboard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();
                            if (humanMovedPiece == null) {
                                sourceTile = null;
                            }
                        } else {
                            destinationTile = chessboard.getTile(tileId);
                            final Move move = Move.moveFactory.createMove(chessboard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
                            final MoveTransition transition = chessboard.getCurrentPlayer().makeMove(move);
                            if (transition.getMoveStatus().isDone()) {
                                chessboard = transition.getTransitionBoard();
                                //TODO add the move to the move log..
                            }
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                        invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                boardPanel.drawBoard(chessboard);
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(final MouseEvent mouseEvent) {

                }

                @Override
                public void mouseReleased(final MouseEvent mouseEvent) {

                }

                @Override
                public void mouseEntered(final MouseEvent mouseEvent) {

                }

                @Override
                public void mouseExited(final MouseEvent mouseEvent) {

                }
            });

            assignTilePieceIcon(chessboard);

            validate();
        }

        private void assignTilePieceIcon(final Board board) {
            this.removeAll();
            if (board.getTile(this.tileId).isTileOccupied()) {
                try {
                    String fullPath = pieceImagesPath + board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0, 1) +
                            board.getTile(this.tileId).getPiece().toString() + pieceImagesExtension;
                    final BufferedImage image = ImageIO.read(new File(fullPath));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        private void assignTileColor() {
            if (BoardUtils.EIGHTH_RANK[this.tileId] ||
                    BoardUtils.SIXTH_RANK[this.tileId] ||
                    BoardUtils.FOURTH_RANK[this.tileId] ||
                    BoardUtils.SECOND_RANK[this.tileId]
                    ) {
                setBackground(this.tileId % 2 == 0 ? LIGHT_TILT_COLOR : DARK_TILE_COLOR);

            } else setBackground(this.tileId % 2 != 0 ? LIGHT_TILT_COLOR : DARK_TILE_COLOR);
        }

        public void drawTile(final Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            validate();
            repaint();
        }
    }

    public enum BoardDirection {
        NORMAL {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);

        abstract BoardDirection opposite();
    }

    private JMenu createPreferencesMenu() {
        final JMenu preferencesMenu = new JMenu("Preferences");
        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
        flipBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessboard);
            }
        });
        preferencesMenu.add(flipBoardMenuItem);
        return preferencesMenu;
    }


}
