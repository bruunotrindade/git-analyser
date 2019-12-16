/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.experiment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author j2cf
 */
public class Git {

    public static List<String> checkout(String repositoryPath, String revision) {
//        String command = "git rev-list --parents -n 1 " + revision;
        String command = "git checkout -f " + revision;

        //System.out.println("\tCommand checkout: " + command);

        List<String> output = new ArrayList<>();

        try {
            Process exec = Runtime.getRuntime().exec(command, null, new File(repositoryPath));

            String s;

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(exec.getErrorStream()));

            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
                String[] split = s.split(" ");
                for (String rev : split) {
                    output.add(rev);
                }

            }

            // read any errors from the attempted command
            /*System.out.println();
            System.out.println("CHECKOUT: ");
            while ((s = stdError.readLine()) != null) {
            	System.out.println(s);
            }*/

        } catch (IOException ex) {
            Logger.getLogger(Git.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return output;
    }

    public static List<String> merge(String repositoryPath, String revision, boolean commit, boolean fastForward) {
//        String command = "git rev-list --parents -n 1 " + revision;
        String command = "git merge ";

        if (!commit) {
            command = command + " --no-commit ";
        }

        if (!fastForward) {
            command = command + "--no-ff ";
        }

        command = command + revision;
        //System.out.println("\tCommand merge: " + command);

        List<String> output = new ArrayList<>();

        try {
            Process exec = Runtime.getRuntime().exec(command, null, new File(repositoryPath));

            String s;

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(exec.getErrorStream()));

            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
//                String[] split = s.split(" ");
//                for (String rev : split) {
//                    output.add(rev);
//                }
                output.add(s);
                //System.out.println(s);
            }

            // read any errors from the attempted command
            /*System.out.println();
            System.out.println("MERGE: ");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }*/

        } catch (IOException ex) {
            Logger.getLogger(Git.class.getName()).log(Level.SEVERE, null, ex);
        }

        return output;
    }

    public static String getMergeBase(String repositoryPath, String commit1, String commit2) {

//        String command = "git rev-list --parents -n 1 " + revision;
        String command = "git merge-base " + commit1 + " " + commit2;

        String output = null;

        try {
            Process exec = Runtime.getRuntime().exec(command, null, new File(repositoryPath));

            String s;

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(exec.getErrorStream()));

            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
                output = s;

            }

            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

        } catch (IOException ex) {
            Logger.getLogger(Git.class.getName()).log(Level.SEVERE, null, ex);
        }

        return output;
    }

    static List<String> reset(String repositoryPath) {

        String command = "git reset --hard ";

        //System.out.println("\tCommand reset: " + command);

        List<String> output = new ArrayList<>();

        try {
            Process exec = Runtime.getRuntime().exec(command, null, new File(repositoryPath));

            String s;

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(exec.getErrorStream()));

            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
                String[] split = s.split(" ");
                for (String rev : split) {
                    output.add(rev);
                }

            }

            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
//                System.out.println(s);
            }

        } catch (IOException ex) {
            Logger.getLogger(Git.class.getName()).log(Level.SEVERE, null, ex);
        }

        return output;
    }

    public static List<String> checkoutMaster(File path) {
        String command = "git checkout master";

        //System.out.println("\tCommand checkout: " + command);

        List<String> output = new ArrayList<>();

        try {
            Process exec = Runtime.getRuntime().exec(command, null, path);

            String s;

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(exec.getErrorStream()));

            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
                String[] split = s.split(" ");
                for (String rev : split) {
                    output.add(rev);
                }

            }

            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
//                System.out.println(s);
            }

        } catch (IOException ex) {
            Logger.getLogger(Git.class.getName()).log(Level.SEVERE, null, ex);
        }

        return output;

    }

    public static String cloneFromURL(String url, String destinationFolder) {

        //StringBuilder result = new StringBuilder();
        String command = "git clone " + url + " " + destinationFolder;

        System.out.println("Cloning project\t" + url);

        try {
            Process exec = Runtime.getRuntime().exec(command);

            String s;

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(exec.getErrorStream()));

            String output = null;
            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
                output = s;

            }

            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

            return output;

        } catch (IOException ex) {
            Logger.getLogger(Git.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }
}
