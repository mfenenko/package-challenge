package com.mobiquityinc.packer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mobiquityinc.exception.APIException;

public class Packer {

	public static String pack(String file) throws APIException {
		Packer packer = new Packer();
		
		List<Challenge> challenges = packer.getChallenges(file);
		
		
		challenges.forEach(challenge -> {
			
			List<Thing> sortedthings = challenge.things.stream().sorted((thing1, thing2) -> {
				return (int)(thing2.weight - thing1.weight);
			}).collect(Collectors.toList());
			
			// Init List of solutions;
			Map<Double, List<Thing>> solutions = new HashMap<>(challenge.things.size());
			
			//
			for (int i = 0; i < sortedthings.size(); i++) {
				
				double iWeight = sortedthings.get(i).weight;
				int iCost   = sortedthings.get(i).cost;
				
				if (iWeight > challenge.totalWeight) continue;
				
				List<Thing> iSolution = new ArrayList<>();
				iSolution.add(sortedthings.get(i));
				
				for (int j = 0; j < sortedthings.size(); j++) {
					
					if (i == j) continue;
					
					double weight = iWeight + sortedthings.get(j).weight;
					int cost = iCost + sortedthings.get(j).cost;
					
					if (weight <= challenge.totalWeight) {
						
						iWeight = weight;
						iCost = cost;
						iSolution.add(sortedthings.get(j));
					}
					
				}
				
				// 
				solutions.put(iWeight * iCost, iSolution);
			}
			
			solutions.entrySet().stream().sorted((entry1, entry2) -> { return (int)(entry2.getKey() - entry1.getKey());}).findFirst().ifPresent(
				entry ->challenge.solution = entry.getValue()
			);
			
		});
		
		return challenges.stream().map(challenge -> {
			return challenge.solution.size() > 0 ? challenge.solution.stream().map(t -> String.valueOf(t.index)).collect(Collectors.joining(",")) : "-";
		}).collect(Collectors.joining("\n"));
	}
	
	List<Challenge> getChallenges(String file) throws APIException {
		List<Challenge> challenges = new ArrayList<>();
		
		// 4
		Challenge challenge = new Challenge();
			challenge.totalWeight = 81;
			challenge.things.add(new Thing(1, 53.38D, 45));
			challenge.things.add(new Thing(2, 88.62D, 98));
			challenge.things.add(new Thing(3, 78.48D, 3));
			challenge.things.add(new Thing(4, 72.30D, 76));
			challenge.things.add(new Thing(5, 30.18D, 9));
			challenge.things.add(new Thing(6, 46.34D, 48));
		challenges.add(challenge);

		// -
		challenge = new Challenge();
			challenge.totalWeight = 8;
			challenge.things.add(new Thing(1, 15.3D, 34));
		challenges.add(challenge);
		
		// 2,7
		challenge = new Challenge();
			challenge.totalWeight = 75;
			challenge.things.add(new Thing(1, 85.31D, 29));
			challenge.things.add(new Thing(2, 14.55D, 74));
			challenge.things.add(new Thing(3, 03.98D, 16));
			challenge.things.add(new Thing(4, 26.24D, 55));
			challenge.things.add(new Thing(5, 63.69D, 52));
			challenge.things.add(new Thing(6, 76.25D, 75));
			challenge.things.add(new Thing(7, 60.02D, 74));
			challenge.things.add(new Thing(8, 93.18D, 35));
			challenge.things.add(new Thing(9, 89.95D, 78));
		challenges.add(challenge);
		
		return challenges;
	}
	
}
