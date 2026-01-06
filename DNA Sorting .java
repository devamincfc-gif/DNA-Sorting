import java.util.ArrayList;

public class DNA {
    private Profile[] database;      
    private String[]  STRsOfInterest; 


    public DNA (String databaseFile, String STRsFile) {

        /*** DO NOT EDIT ***/
        createDatabaseOfProfiles(databaseFile); 
        readSTRsOfInterest(STRsFile);           
    }

 
    public void createDatabaseOfProfiles (String filename) {

        StdIn.setFile(filename); // DO NOT remove this line, keep it as the first line in the method.

        int p = StdIn.readInt();
        StdIn.readLine();

        database = new Profile[p];
        
        for (int i = 0; i < p; i++) {

            String name  = StdIn.readString();
            String sequence1 = StdIn.readString();
            String sequence2 = StdIn.readString();

            Profile profile  = new Profile(name, null, null, sequence1, sequence2);
            database[i] = profile;

        }

    }

    public void readSTRsOfInterest (String filename) {

        StdIn.setFile(filename); // DO NOT remove this line, keep as the first line in the method.

        int s = StdIn.readInt();

        StdIn.readLine();

        STRsOfInterest = new String[s];

        for (int i = 0; i < s; i++) {

            STRsOfInterest[i] = StdIn.readString();
        
        }

    }

    public Profile createUnknownProfile (String filename) {
 
	    StdIn.setFile(filename); // DO NOT remove this line, keep as the first line in the method.

        String sequence1 = StdIn.readString();
        String sequence2 = StdIn.readString();

        Profile unknownProfile = new Profile("Unknown", null, null, sequence1, sequence2);
        
        return unknownProfile;
    }


    public STR findSTRInSequence (String sequence,String STR ) {
        
        int temp = 0;
        int count = 0;
        int condition = 0;
    
        while ((temp = sequence.indexOf(STR, temp)) >= 0) {
            count = 1;
            temp += STR.length();
    
            while (temp + STR.length() <= sequence.length() && sequence.substring(temp, temp + STR.length()).equals(STR)) {
                count += 1;
                temp = temp + STR.length();
            }
    
            if (count > condition) {
                condition = count;
            }
        }
    
        return new STR(STR, condition);
    
    }
    

    public void createProfileSTRs ( Profile profile, String[] allSTRs ) {

        STR[] strs1 = new STR[allSTRs.length];
        STR[] strs2 = new STR[allSTRs.length];

        
        for (int i = 0; i<allSTRs.length; i++) {

            String temp = allSTRs[i];

            strs1[i] = findSTRInSequence(profile.getSequence1(), temp);
            strs2[i] = findSTRInSequence(profile.getSequence2(), temp);
        }

        profile.setS1_STRs(strs1);
        profile.setS2_STRs(strs2);

    }

    public void createDatabaseSTRs() {

        for (Profile profile : database) {
            createProfileSTRs(profile, STRsOfInterest);
        }

    }

    public boolean identicalSTRs ( STR[] s1, STR[] s2 ) {

        if (s1.length != s2.length) {return false;}

        for (int i = 0; i < s1.length; i++) {
            if (!s1[i].equals(s2[i])) {
                return false;
            }
        }

        return true;
    }

    public ArrayList<Profile> findMatchingProfiles ( STR[] unknownProfileS1_STRs ) {
        
        ArrayList<Profile> temp = new ArrayList<>();

        for (Profile profile : database) {

            if (identicalSTRs(profile.getS1_STRs(), unknownProfileS1_STRs)) {

                temp.add(profile);
                
            }
        }
    
        return temp;

    }

    public boolean punnetSquare(STR[] firstParent,  STR[] inheritedFromFirstParent, 
                                STR[] secondParent, STR[] inheritedFromSecondParent) {

        /* DO NOT EDIT */

        for ( int i = 0; i < firstParent.length; i++ ) {

            if (!(firstParent[i].equals(inheritedFromFirstParent[i]) && secondParent[i].equals(inheritedFromSecondParent[i]))) {
                return false; // Returns false if there is a discrepency
            }
        }
        return true; 
    }


    public ArrayList<Profile> findPossibleParents(STR[] S1_STRs, STR[] S2_STRs) {

        ArrayList<Profile> possibleParent1 = new ArrayList<Profile>();
        ArrayList<Profile> possibleParent2 = new ArrayList<Profile>();

        for (int i = 0; i < database.length; i++) {
            if (identicalSTRs(database[i].getS2_STRs(), S1_STRs)) {
                possibleParent1.add(database[i]);
            }
            if (identicalSTRs(database[i].getS1_STRs(), S2_STRs)) {
                 possibleParent2.add(database[i]);
            }
            if (identicalSTRs(database[i].getS1_STRs(), S1_STRs)) {
                 possibleParent1.add(database[i]);
            }
            if (identicalSTRs(database[i].getS2_STRs(), S2_STRs)) {
                 possibleParent2.add(database[i]);
            }
        }

        ArrayList<Profile> parentList = new ArrayList<>(); 

        for (int p1 = 0; p1 < possibleParent1.size(); p1++) {
            for (int p2 = 0; p2 < possibleParent2.size(); p2++) {
                if (!possibleParent1.get(p1).equals(possibleParent2.get(p2)) ) {
            
                    if (punnetSquare(possibleParent2.get(p2).getS2_STRs(), S2_STRs,
                            possibleParent1.get(p1).getS2_STRs(), S1_STRs)) {
                        parentList.add(possibleParent1.get(p1));
                        parentList.add(possibleParent2.get(p2));
                    }
                    else if (punnetSquare(possibleParent2.get(p2).getS1_STRs(), S1_STRs,
                            possibleParent1.get(p1).getS1_STRs(), S1_STRs)) {
                        parentList.add(possibleParent1.get(p1));
                        parentList.add(possibleParent2.get(p2));
                    }
                    else if (punnetSquare(possibleParent2.get(p2).getS2_STRs(), S2_STRs,
                            possibleParent1.get(p1).getS2_STRs(), S2_STRs)) {
                        parentList.add(possibleParent1.get(p1));
                        parentList.add(possibleParent2.get(p2));
                    }
                    else if (punnetSquare(possibleParent2.get(p2).getS1_STRs(), S2_STRs,
                            possibleParent1.get(p1).getS1_STRs(), S1_STRs)) {
                        parentList.add(possibleParent1.get(p1));
                        parentList.add(possibleParent2.get(p2));
                    }
                }
            }
        }
        return parentList; 
    }


    public Profile[] getDatabase() {

        /* DO NOT EDIT */
        return database;
    }

    public String[] getSTRsOfInterest() {

        /* DO NOT EDIT */
        return STRsOfInterest;
    }
}
