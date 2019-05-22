package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.Player;
import com.chess.engine.player.ai.MinMax;
import com.chess.engine.player.ai.MoveStrategy;
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
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static javax.swing.SwingUtilities.*;

/**
 * Created By Mahamdi Amine on 12/23/17
 */
public class Table extends Observable {
    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(700, 600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
    private final Color LIGHT_TILT_COLOR = Color.decode("#FFFFFF");
    private final Color DARK_TILE_COLOR = Color.decode("#1D3D63");
    private final JFrame gameFrame;
    private final GameHistoryPanel gameHistoryPanel;
    private final TakenPiecesPanel takenPiecesPanel;
    private final BoardPanel boardPanel;
    private final MoveLog moveLog;
    private Move computerMove;
    private final GameSetup gameSetup;
    private Board chessboard;
    private BoardDirection boardDirection;
    private boolean highlightLegalMoves;
    private static String pieceImagesPath = "Images/pieces/";
    private static String bimPath = "Images/others/bim2.png";
    private static String pieceImagesExtension = ".png";
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private static final Table INSTANCE = new Table();

    private Table() {
        this.chessboard = Board.createStandardBoard();
        this.moveLog = new MoveLog();
        this.addObserver(new TableGameAIWatcher());
        this.boardDirection = BoardDirection.NORMAL;
        this.highlightLegalMoves = false;
        this.gameFrame = new JFrame("Chess is my life ");
        this.boardPanel = new BoardPanel();
        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setLayout(new BorderLayout());
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameSetup = new GameSetup(this.gameFrame, true);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameFrame.setVisible(true);
        this.gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);
    }

    public static Table get() {
        return INSTANCE;
    }

    public GameSetup getGameSetup() {
        return this.gameSetup;
    }

    private Board getGameBoard() {
        return this.chessboard;
    }

    private void setupUpdate(final GameSetup gameSetup) {
        setChanged();
        notifyObservers(gameSetup);
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        tableMenuBar.add(createOptionsMenu());
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

    private JMenu createOptionsMenu() {
        final JMenu optionsMenu = new JMenu("Options");
        final JMenuItem setupGameMenuItem = new JMenuItem("Setup Game");
        setupGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Table.get().getGameSetup().promptUser();
                Table.get().setupUpdate(Table.get().getGameSetup());
            }
        });
        optionsMenu.add(setupGameMenuItem);
        return optionsMenu;
    }

    private MoveLog getMoveLog() {
        return this.moveLog;
    }

    private GameHistoryPanel getGameHistoryPanel() {
        return this.gameHistoryPanel;
    }

    public TakenPiecesPanel getTakenPiecesPanel() {
        return this.takenPiecesPanel;
    }

    public BoardPanel getBoardPanel() {
        return this.boardPanel;
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
            assignTilePieceIcon(chessboard);
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
                                moveLog.addMove(move);
                            }
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                gameHistoryPanel.redo(chessboard, moveLog);
                                takenPiecesPanel.redo(moveLog);
                                if (gameSetup.isAIPlayer(chessboard.getCurrentPlayer())) {
                                    Table.get().moveMadeUpdate(PlayerType.HUMAN);
                                }
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
            highlightLegals(chessboard);
            validate();
            repaint();
        }

        private void highlightLegals(final Board board) {
            if (highlightLegalMoves) {
                for (final Move move : pieceLegalMoves(board)) {
                    if (move.getDestinationCoordinate() == this.tileId) {
                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File(bimPath)))));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }

        private Collection<Move> pieceLegalMoves(final Board board) {
            if (humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.getCurrentPlayer().getAlliance()) {
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }
    }

    protected class MoveLog {
        private final List<Move> moves;

        MoveLog() {
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves() {
            return moves;
        }

        public void addMove(final Move move) {
            this.moves.add(move);
        }

        public boolean removeMove(final Move move) {
            return this.moves.remove(move);
        }

        public Move removeMove(final int index) {
            return this.moves.remove(index);
        }

        void clear() {
            this.moves.clear();
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
        preferencesMenu.addSeparator();
        final JCheckBoxMenuItem legalMoveHighlighterCheckBox = new JCheckBoxMenuItem("Highlight Legal Moves", false);
        legalMoveHighlighterCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                highlightLegalMoves = legalMoveHighlighterCheckBox.isSelected();
            }
        });
        preferencesMenu.add(legalMoveHighlighterCheckBox);
        return preferencesMenu;
    }

    enum PlayerType {
        HUMAN, COMPUTER
    }

    private static class TableGameAIWatcher implements Observer {
        @Override
        public void update(final Observable observable, final Object o) {
            if (Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().getCurrentPlayer())
                    && !Table.get().getGameBoard().getCurrentPlayer().isInStaleMate()
                    && !Table.get().getGameBoard().getCurrentPlayer().isInCheckMate()) {
                final AIThink aiThink = new AIThink();
                aiThink.execute();
            } else {
                if (Table.get().getGameBoard().getCurrentPlayer().isInStaleMate()) {
                    //    JOptionPane.showMessageDialog();
                    System.out.println("stale mate");
                }
                if (Table.get().getGameBoard().getCurrentPlayer().isInCheckMate()) {
                    //    JOptionPane.showMessageDialog();
                    System.out.println("check mate");
                }
            }

        }
    }

    private static class AIThink extends SwingWorker<Move, String> {

        private AIThink() {

        }

        @Override
        protected void done() {
            try {
                final Move bestMove = get();
                Table.get().updateComputerMove(bestMove);
                Table.get().updateGameBoard(Table.get().getGameBoard().getCurrentPlayer().makeMove(bestMove).getTransitionBoard());
                Table.get().getMoveLog().addMove(bestMove);
                Table.get().getGameHistoryPanel().redo(Table.get().getGameBoard(), Table.get().getMoveLog());
                Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
                Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
                // Table.get().getDebugPanel().redo();
                Table.get().moveMadeUpdate(PlayerType.COMPUTER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Move doInBackground() throws Exception {
            final MoveStrategy miniMax = new MinMax(4);
            final Move bestMove = miniMax.execute(Table.get().getGameBoard());
            return bestMove;
        }
    }

    private void moveMadeUpdate(final PlayerType playerType) {
        setChanged();
        notifyObservers(playerType);
    }

    private void updateComputerMove(final Move move) {
        this.computerMove = move;
    }


    private void updateGameBoard(final Board board) {
        this.chessboard = board;
    }

    public void show() {
        Table.get().getMoveLog().clear();
        Table.get().getGameHistoryPanel().redo(chessboard, Table.get().getMoveLog());
        Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
        //Table.get().getDebugPanel().redo();
    }

}
