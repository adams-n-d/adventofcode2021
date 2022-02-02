import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class PowerConsumption
{
    public static void main(String[] p_args)
    {
        String filename = "input.txt";
		try (Stream<String> stream = Files.lines(Paths.get(filename)))
        {
            process(stream);
		}
        catch (IOException e)
        {
			e.printStackTrace();
            return;
		}
    }

    private static void process(Stream<String> p_stream)
    {
        // Assume bit-string of length 12
        int[] sums = new int[12];
        for (int i=0; i<sums.length; ++i)
        {
            sums[i] = 0;
        }

        String[] array = p_stream.toArray(String[]::new);
        for (String b : array)
        {
            for (int i=0; i<sums.length; ++i)
            {
                if ('1' == b.charAt(i))
                {
                    sums[i]++;
                }
            }
        }

        StringBuilder gamma = new StringBuilder();
        StringBuilder epsilon = new StringBuilder();
        for (int i=0; i<sums.length; ++i)
        {
            double avg = sums[i] / Double.valueOf(array.length);
            if (avg > 0.5)
            {
                // 1 most common
                gamma.append("1");
                epsilon.append("0");
            }
            else
            {
                gamma.append("0");
                epsilon.append("1");
            }
        }
        Short gammaS = Short.parseShort(gamma.toString(), 2);
        Short epsilonS = Short.parseShort(epsilon.toString(), 2);
        System.out.println("gamma: " + gammaS);
        System.out.println("epsilon: " + epsilonS);
        System.out.println("power: " + gammaS*epsilonS);
    }
}
