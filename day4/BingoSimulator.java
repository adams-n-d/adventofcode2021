
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import java.util.List;
import java.util.ArrayList;

public class BingoSimulator
{
    List<BingoBoard> boards;
    // Assume boards are N x N squares
    public final static int N = 5;
    int[] drawnNumbers;

    public BingoSimulator(String p_filename)
    {
        boards = new ArrayList<BingoBoard>();
		try (Stream<String> stream = Files.lines(Paths.get(p_filename)))
        {
            process(stream);
		}
        catch (IOException e)
        {
			e.printStackTrace();
            return;
		}
        for (BingoBoard board : boards)
        {
            board.printBoard();
        }
    }

    private void process(Stream<String> p_stream)
    {
        //List<String> list = p_stream.collect(Collectors.toList());
        String[] list = p_stream
            .map(String::trim)
            .toArray(String[]::new);

        for (int i=0; i<list.length; ++i)
        {
            String line = list[i];
            if (line.matches(".*,.*"))
            {
                log("getting drawnNumbers");
                String[] nbrs = line.split(",");
                drawnNumbers = new int[nbrs.length];
                for (int j=0; j<nbrs.length; ++j)
                {
                    drawnNumbers[j] = Integer.valueOf(nbrs[j]);
                }
            }
            else if (line.split("\\s+").length == N)
            {
                int[] boardNbrs = new int[N*N];
                for (int j=0; j<N; ++j)
                {
                    // Get board row by row.
                    String[] row = line.split("\\s+");
                    for (int k=0; k<N; ++k)
                    {
                        boardNbrs[j*N + k] = Integer.valueOf(row[k]);
                    }

                    // Increment so we don't read this line again.
                    ++i;
                    if (i < list.length)
                    {
                        line = list[i];
                    }

                }
                boards.add(new BingoBoard(boardNbrs));
            }
        }
    }
    public void findLastBoardToWin()
    {
        for (int i=0; i<drawnNumbers.length; ++i)
        {
            markAllBoards(drawnNumbers[i]);
            testAllBoardsAndRemoveWinners(drawnNumbers[i]);
        }
    }

    public void run()
    {
        for (int i=0; i<drawnNumbers.length; ++i)
        {
            markAllBoards(drawnNumbers[i]);
            if (testAllBoards(drawnNumbers[i]))
            {
                return;
            }
        }
    }


    public boolean testAllBoardsAndRemoveWinners(int i)
    {
        boolean gotFinal = false;
        List<BingoBoard> losers = new ArrayList<BingoBoard>();
        for (BingoBoard board : boards)
        {
            if (board.testForWin())
            {
                board.printBoard();
                log("Found winning board: " + board.getUnmarkedPoints() * i);
                if (boards.size() == 1)
                {
                    gotFinal = true;
                    log("Found last to win with draw " + i + ": " + board.getUnmarkedPoints() * i);
                }
            }
            else
            {
                losers.add(board);
            }
        }
        boards = losers;
        return gotFinal;
    }
    public boolean testAllBoards(int i)
    {
        boolean won = false;
        for (BingoBoard board : boards)
        {
            if (board.testForWin())
            {
                board.printBoard();
                log("Found winning board: " + board.getUnmarkedPoints() * i);
                won = true;
            }
        }
        return won;
    }

    public void markAllBoards(int i)
    {
        for (BingoBoard board : boards)
        {
            board.mark(i);
        }
    }

    public static void main(String [] p_args)
    {
        String fileName = "input.txt";
        //String fileName = "test.txt";
        BingoSimulator bs = new BingoSimulator(fileName);
        //bs.run();
        bs.findLastBoardToWin();
    }

    private class BingoBoard
    {
        // NxN array representing the board.
        //  Indices 0..N-1 show the first row.
        //          N..2N-1 shows the second row, and so on.
        int[] boardNumbers;

        boolean[]  isMarked;

        private BingoBoard(int[] NN)
        {
            boardNumbers = NN;
            isMarked = new boolean[NN.length];
            for (int i=0; i<isMarked.length; ++i)
            {
                isMarked[i] = false;
            }
        }
        // Place a virtual marker on p_number on the
        // Bingo board.
        public void mark(int p_number)
        {
            for (int i=0; i<N*N; ++i)
            {
                if (boardNumbers[i] == p_number)
                {
                    log("Marking " + p_number + " at index "+ i);
                    isMarked[i] = true;
                }
            }
        }

        // Return true of board is a winning board.
        public boolean testForWin()
        {
            // Test each row.
            for (int i=0; i<N; ++i)
            {
                boolean allTrue = true;
                for (int j=0; j<N; ++j)
                {
                    if (!isMarked[i*N + j])
                    {
                        allTrue = false;
                    }
                }
                if (allTrue)
                {
                    return true;
                }
            }
            
            // Test each column.
            for (int j=0; j<N; ++j)
            {
                boolean allTrue = true;
                for (int i=0; i<N; ++i)
                {
                    if (!isMarked[i*N + j])
                    {
                        allTrue = false;
                    }
                }
                if (allTrue)
                {
                    return true;
                }
            }
            return false;
        }

        public int getUnmarkedPoints()
        {
            int retVal = 0;
            for (int i=0; i<N*N; ++i)
            {
                if (!isMarked[i])
                {
                    log("Adding " + boardNumbers[i] + "from index " + i);
                    retVal += boardNumbers[i];
                }
            }
            return retVal;
        }

        public void printBoard()
        {
            StringBuilder sb = new StringBuilder();
            sb.append("Board:\n");
            for (int i=0; i<N*N; ++i)
            {
                sb.append(String.valueOf(boardNumbers[i]));
                if (i % N == 4) // is last row
                {
                    sb.append("\n");
                }
                else
                {
                    sb.append(" ");
                }
            }
            sb.append("\n");
            log(sb.toString());
        }

    }

    public void log(String msg)
    {
        System.out.println(msg);
    }
}
