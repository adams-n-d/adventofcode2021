import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class LifeSupportRating
{
    // TODO: refactor to make these readable and maintainable.
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
        String[] array = p_stream.toArray(String[]::new);

        String o2genRating = findO2(array);

        System.out.println("O2 gen rating: " + o2genRating);

        String co2genRating = findCO2(array);

        System.out.println("CO2 gen rating: " + co2genRating);

        Integer o2 = Integer.parseInt(o2genRating, 2);
        System.out.println("O2 int: " + o2);
        Integer co2 = Integer.parseInt(co2genRating, 2);
        System.out.println("CO2 int: " + co2);

        System.out.println("product: " + o2 * co2);
    }


    private static String findO2(String[] p_array)
    {
        List<String> list = new ArrayList<String>(Arrays.asList(p_array));

        return findO2Values(list, 0);
    }
    private static String findO2Values(List<String> p_list, int p_index)
    {
        System.out.println("index: " + p_index);
        if (p_list.size() == 1)
        {
            return p_list.get(0);
        }
        int sum = 0;
        for (String b : p_list)
        {
            if ('1' == b.charAt(p_index))
            {
                sum++;
            }
        }
        double avg = sum / Double.valueOf(p_list.size());
        final char mostCommon;
        if (avg >= 0.5)
        {
            mostCommon = '1';
        }
        else
        {
            mostCommon = '0';
        }
        System.out.println("most common: " + mostCommon);

        // Have most common digit. Can now prune list.
        List<String> mutableList = new ArrayList<String>(p_list);
        for (String s : p_list)
        {
            if (mostCommon != s.charAt(p_index))
            {
                System.out.println("purging: " + s);
                removeFromList(s, mutableList);
            }
        }

        return findO2Values(mutableList, p_index+1);
    }

    private static void removeFromList(String p_str, List<String> p_list)
    {
        while(p_list.remove(p_str))
        {}
    }

    private static String findCO2(String[] p_array)
    {
        List<String> list = new ArrayList<String>(Arrays.asList(p_array));

        return findCO2Values(list, 0);
    }
    private static String findCO2Values(List<String> p_list, int p_index)
    {
        System.out.println("index: " + p_index);
        if (p_list.size() == 1)
        {
            return p_list.get(0);
        }
        int sum = 0;
        for (String b : p_list)
        {
            if ('1' == b.charAt(p_index))
            {
                sum++;
            }
        }
        double avg = sum / Double.valueOf(p_list.size());
        final char leastCommon;
        if (avg < 0.5)
        {
            leastCommon = '1';
        }
        else
        {
            leastCommon = '0';
        }
        System.out.println("leastCommon: " + leastCommon);

        // Have least common digit. Can now prune list.
        List<String> mutableList = new ArrayList<String>(p_list);
        for (String s : p_list)
        {
            if (leastCommon != s.charAt(p_index))
            {
                System.out.println("purging: " + s);
                removeFromList(s, mutableList);
            }
        }

        return findCO2Values(mutableList, p_index+1);
    }
}
