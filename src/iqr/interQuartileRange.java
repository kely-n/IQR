package iqr;
import java.util.*;
import java.util.stream.Stream;

public class interQuartileRange {

    private final HashMap mainHash = new HashMap(50);

    //
    private final HashMap firstHalf = new HashMap(25);
    private final HashMap secondHalf = new HashMap(25);

    //form int array out of input string

    private final HashMap firstFirstHalf = new HashMap(25);
    private final HashMap secondFirstHalf = new HashMap(25);

    private final HashMap firsSecondtHalf = new HashMap(25);
    private final HashMap secondSecondHalf = new HashMap(25);

    public int[] stripString(String input){
        int[] elements= Stream.of(input.split(" ")).mapToInt(Integer::parseInt).toArray();
        return elements;
    }

    private int[] sortArray(int[] array){
        Arrays.parallelSort(array);
         return array;
    }

    private HashMap setUpHashmap(HashMap m, int[] elements, int[] frequencies){
        for(int i = 0; i<elements.length; i++){
            m.put(elements[i], frequencies[i]);
        }
        return m;
    }

    int midpoint;

    //receives sorted elements.
    private void divideMainHash(int[] elements, int totalFrequency){

        boolean isodd = true;
        boolean medianHasNotBeenRemoved = true;
        //find midpoint
        //depends with whether its odd or even.
        if(totalFrequency%2==0){
            midpoint = totalFrequency/2;
            isodd = false;

        }else {
            midpoint = ((totalFrequency-1)/2);
        }
        //if sum is less than or equal to midpoint add to firstHalf
        //if sum is greater than midpoint add to secondHalf
        //if totalfrequency was odd, the skip the median.
        //previous sum put in array
        int current_sum = 0;
        //sum after adding current element frequency
        int sum = 0;
       for(int element : elements){
           int f = (int) mainHash.get(element);
           sum+=f;
           if(sum<=midpoint){
               firstHalf.put(element, mainHash.get(element));
           }else if(sum>midpoint && current_sum<=midpoint){
               //find amount left to reach midpoint, and add it with value to firstHalf
               int amount_left_to_reach_Midpoint =  midpoint - current_sum;
               if(amount_left_to_reach_Midpoint>0) {
                   firstHalf.put(element, amount_left_to_reach_Midpoint);
               }
               //now find amount left from current frequency, which is either
               //current frequency f - amount_left_to_reach_Midpoint or
               //sum - midpoint
               int amount_left_from_current_frequency = sum - midpoint;
               //if totalfrequency was odd, remove one from amount_left_from_current_frequency, which will be the midpoint,
               //if even nothing, then add the amount left to the secondHalf
               if(isodd){
                   //remove the median, which wont appear in the firstHalf or secondHalf
                   amount_left_from_current_frequency-=1;
                   medianHasNotBeenRemoved = false;
                   //update current sum
                   //current_sum+=1;
               }
               //if amount_left_from_current_frequency is not 0 add it to the secondHalf.
               if(amount_left_from_current_frequency>0){
                   secondHalf.put(element,amount_left_from_current_frequency);
               }

           }else {
               if(isodd && medianHasNotBeenRemoved){
                   System.out.println(element);
                   if(mainHash.containsKey(element)) {
                       int amount_after_removing_median = (int) mainHash.get(element) - 1;
                       if (amount_after_removing_median > 0) {
                           secondHalf.put(element, amount_after_removing_median);
                       }
                   }
                   medianHasNotBeenRemoved = false;
               }else {
                   secondHalf.put(element,mainHash.get(element));
               }

           }
          current_sum = sum;
       }
    }

    private double dividefirstHalf(int[] elements, int totalFrequency){
        int midpoint;
        boolean isodd = true;
        boolean medianHasNotBeenRemoved = true;
        //find midpoint
        //depends with whether its odd or even.
        if(totalFrequency%2==0){
            midpoint = totalFrequency/2;
            isodd = false;

        }else {
            midpoint = ((totalFrequency-1)/2);
        }
        //if is even, grab the the last element in the first part and first element in second part
        int first_num = 0;
        int second_num = 0;
        //if sum is less than or equal to midpoint add to firstHalf
        //if sum is greater than midpoint add to secondHalf
        //if totalfrequency was odd, the skip the median.
        //previous sum put in array
        int current_sum = 0;
        //sum after adding current element frequency
        int sum = 0;
        for(int element : elements){
            int f = (int) firstHalf.get(element);
            sum+=f;
            if(sum<=midpoint){
                firstFirstHalf.put(element, firstHalf.get(element));
                first_num = element;
            }else if(sum>midpoint && current_sum<=midpoint){
                //find amount left to reach midpoint, and add it with value to firstHalf
                int amount_left_to_reach_Midpoint =  midpoint - current_sum;
                if(amount_left_to_reach_Midpoint>0) {
                    firstFirstHalf.put(element, amount_left_to_reach_Midpoint);
                    first_num = element;
                }

                //set current_sum to midpoint or add amount_left_to_reach_Midpoint to the current sum
                current_sum += amount_left_to_reach_Midpoint;
                //now find amount left from current frequency, which is either
                //current frequency f - amount_left_to_reach_Midpoint or
                //sum - midpoint
                int amount_left_from_current_frequency = sum - midpoint;
                //if totalfrequency was odd, remove one from amount_left_from_current_frequency, which will be the midpoint,
                //if even nothing, then add the amount left to the secondHalf
                if(isodd){
                    //remove the median, which wont appear in the firstHalf or secondHalf
                    amount_left_from_current_frequency-=1;
                    medianHasNotBeenRemoved = false;
                    return element;
                    //update current sum
                    //current_sum+=1;
                }
                //if amount_left_from_current_frequency is not 0 add it to the secondHalf.
                if(amount_left_from_current_frequency>0){
                    secondFirstHalf.put(element,amount_left_from_current_frequency);
                    second_num = element;
                }

            }else {
                if(isodd && medianHasNotBeenRemoved){
                    if(firstHalf.containsKey(element)) {
                        int amount_after_removing_median = (int) firstHalf.get(element) - 1;
                        if (amount_after_removing_median > 0) {
                            secondFirstHalf.put(element, amount_after_removing_median);
                        }
                        second_num = element;
                    }
                    medianHasNotBeenRemoved = false;

                }else {
                    //if second num has not been set, grab it first
                    if(second_num==0){
                        second_num = element;
                    }
                    secondFirstHalf.put(element,firstHalf.get(element));
                }

            }
            current_sum = sum;
        }
        //return the median
        //System.out.println("first: "+first_num+" second: "+second_num);
        double a = (double) first_num;
        double b = (double) second_num;
        return (a+b)/2;
    }

    private double divideSecondHalf(int[] elements, int totalFrequency){
        int midpoint;
        boolean isodd = true;
        boolean medianHasNotBeenRemoved = true;
        //find midpoint
        //depends with whether its odd or even.
        if(totalFrequency%2==0){
            midpoint = totalFrequency/2;
            isodd = false;

        }else {
            midpoint = ((totalFrequency-1)/2);
        }
        //if is even, grab the the last element in the first part and first element in second part
        int first_num = 0;
        int second_num = 0;
        //if sum is less than or equal to midpoint add to firstHalf
        //if sum is greater than midpoint add to secondHalf
        //if totalfrequency was odd, the skip the median.
        //previous sum put in array
        int current_sum = 0;
        //sum after adding current element frequency
        int sum = 0;
        for(int element : elements){
            int f = (int) secondHalf.get(element);
            sum+=f;
            if(sum<=midpoint){
                firsSecondtHalf.put(element, secondHalf.get(element));
                first_num = element;
            }else if(sum>midpoint && current_sum<=midpoint){
                //find amount left to reach midpoint, and add it with value to firstHalf
                int amount_left_to_reach_Midpoint =  midpoint - current_sum;
                if(amount_left_to_reach_Midpoint>0) {
                    firsSecondtHalf.put(element, amount_left_to_reach_Midpoint);
                    first_num = element;
                }
                //set current_sum to midpoint or add amount_left_to_reach_Midpoint to the current sum
                current_sum += amount_left_to_reach_Midpoint;
                //now find amount left from current frequency, which is either
                //current frequency f - amount_left_to_reach_Midpoint or
                //sum - midpoint
                int amount_left_from_current_frequency = sum - midpoint;
                //if totalfrequency was odd, remove one from amount_left_from_current_frequency, which will be the midpoint,
                //if even nothing, then add the amount left to the secondHalf
                if(isodd){
                    //remove the median, which wont appear in the firstHalf or secondHalf
                    amount_left_from_current_frequency-=1;
                    medianHasNotBeenRemoved = false;
                    return element;
                    //update current sum
                    //current_sum+=1;
                }
                //if amount_left_from_current_frequency is not 0 add it to the secondHalf.
                if(amount_left_from_current_frequency!=0){
                    secondSecondHalf.put(element,amount_left_from_current_frequency);
                    second_num = element;
                }

            }else {
                if(isodd && medianHasNotBeenRemoved){
                    if(secondHalf.containsKey(element)) {
                        int amount_after_removing_median = (int) secondHalf.get(element) - 1;
                        if (amount_after_removing_median > 0) {
                            secondSecondHalf.put(element, amount_after_removing_median);
                        }
                        second_num = element;
                    }
                    medianHasNotBeenRemoved = false;
                }else {
                    //if second num has not been set, grab it first
                    if(second_num==0){
                        second_num = element;
                    }
                    secondSecondHalf.put(element,secondHalf.get(element));
                }

            }
            current_sum = sum;
        }
        //return the median
        //System.out.println("first: "+first_num+" second: "+second_num);
        double a = (double) first_num;
        double b = (double) second_num;
        return (a+b)/2;
    }
    public static void main(String ... args){
        Iterator it;
        interQuartileRange interQuartileRange = new interQuartileRange();
        Scanner sc = new Scanner(System.in);

        //get input
        System.out.print("Value of n:  ");
        int n = Integer.valueOf(sc.nextLine());
        //System.out.println();
        System.out.print("Elements: ");
        int[] elements = interQuartileRange.stripString(sc.nextLine());
        //System.out.println();
        System.out.print("Frequencies:  ");
        int[] frequencies = interQuartileRange.stripString(sc.nextLine());
        if(elements.length != n){
            System.out.println("error in number elements!");
        }else if(frequencies.length != n){
            System.out.println("error in number frequencies!");
        }else{
            //set up the main row of values
            //assigns element as key and frequency as value.
            interQuartileRange.setUpHashmap(interQuartileRange.mainHash , elements, frequencies);
            //sort the elements
            elements  = interQuartileRange.sortArray(elements);

            //divide elements into 2
            //find sumation of frequencies
            int sum = 0;
            for(int frequency : frequencies){
                sum+=frequency;
            }

            //divide the mainHash
            interQuartileRange.divideMainHash(elements,sum);

            //confirming if first division was right
            //print values.
    /*
            it = interQuartileRange.firstHalf.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry obj  = (Map.Entry)it.next();
                System.out.println(obj.getKey()+" : "+obj.getValue());
            }
            System.out.println("second half");
            it = interQuartileRange.secondHalf.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry obj  = (Map.Entry)it.next();
                System.out.println(obj.getKey()+" : "+obj.getValue());
            }

     */

            //get elements and frequency values from firstHalf and secondHalf.
            int[] firstHalf_elements = new int[interQuartileRange.firstHalf.size()];
            int summation_frequency_firstHalf = 0;
            //firstHalf
            it = interQuartileRange.firstHalf.entrySet().iterator();
            int i = 0;
            if (it.hasNext()) {
                do {
                    Map.Entry obj = (Map.Entry) it.next();
                    firstHalf_elements[i] = (int) obj.getKey();
                    summation_frequency_firstHalf += (int) obj.getValue();
                    i++;
                } while (it.hasNext());
            }
            //sort array and divide it further into 2
            firstHalf_elements = interQuartileRange.sortArray(firstHalf_elements);
            double lowerQuartile = interQuartileRange.dividefirstHalf(firstHalf_elements, summation_frequency_firstHalf);
            System.out.println("lower quartile: "+lowerQuartile);
            //print it
            //confirming if division was right
/*
            System.out.println("firstFirstHalf");
            it = interQuartileRange.firstFirstHalf.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry obj  = (Map.Entry)it.next();
                System.out.println(obj.getKey()+" : "+obj.getValue());
            }
            System.out.println("secondFirstHalf");
            it = interQuartileRange.secondFirstHalf.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry obj  = (Map.Entry)it.next();
                System.out.println(obj.getKey()+" : "+obj.getValue());
            }


 */


            //second half
            int[] secondHalf_elements = new int[interQuartileRange.secondHalf.size()];
            int summation_frequency_secondHalf = 0;
            //firstHalf
            it = interQuartileRange.secondHalf.entrySet().iterator();
            i = 0;
            while (it.hasNext()){
                Map.Entry obj  = (Map.Entry)it.next();
                secondHalf_elements[i] = (int) obj.getKey();
                summation_frequency_secondHalf+= (int)obj.getValue();
                i++;
            }
            //sort array and divide it further into 2
            //check if
            secondHalf_elements = interQuartileRange.sortArray(secondHalf_elements);
            double upperQuartile = interQuartileRange.divideSecondHalf(secondHalf_elements, summation_frequency_secondHalf);
            System.out.println("upper quartile: "+ upperQuartile);

            //confirming if division is right
/*
            System.out.println(" first second half");
            it = interQuartileRange.firsSecondtHalf.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry obj  = (Map.Entry)it.next();
                System.out.println(obj.getKey()+" : "+obj.getValue());

            }

            System.out.println(" second second half");
            it = interQuartileRange.secondSecondHalf.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry obj  = (Map.Entry)it.next();
                System.out.println(obj.getKey()+" : "+obj.getValue());

            }



 */
            System.out.println("interQuartile range: " +(upperQuartile - lowerQuartile));
        }
    }

}
