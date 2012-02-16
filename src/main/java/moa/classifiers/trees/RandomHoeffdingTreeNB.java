/*
 *    RandomHoeffdingTreeNB.java
 *    Copyright (C) 2010 University of Waikato, Hamilton, New Zealand
 *    @author Albert Bifet (abifet@cs.waikato.ac.nz)
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package moa.classifiers.trees;

import moa.classifiers.bayes.NaiveBayes;
import moa.options.IntOption;
import weka.core.Instance;

/**
 * Random decision trees for data streams that uses naive Bayes learners at the
 * leaves.
 *
 * @author Albert Bifet (abifet at cs dot waikato dot ac dot nz)
 * @version $Revision: 7 $
 */
public class RandomHoeffdingTreeNB extends RandomHoeffdingTree {

    private static final long serialVersionUID = 1L;

    @Override
    public String getPurposeString() {
        return "Random decision trees for data streams that uses naive Bayes learners at the leaves.";
    }

    public IntOption nbThresholdOption = new IntOption(
            "nbThreshold",
            'q',
            "The number of instances a leaf should observe before permitting Naive Bayes.",
            0, 0, Integer.MAX_VALUE);

    public static class LearningNodeNB extends RandomLearningNode {

        private static final long serialVersionUID = 1L;

        public LearningNodeNB(double[] initialClassObservations) {
            super(initialClassObservations);
        }

        @Override
        public double[] getClassVotes(Instance inst, HoeffdingTree ht) {
            if (getWeightSeen() >= ((HoeffdingTreeNB) ht).nbThresholdOption.getValue()) {
                return NaiveBayes.doNaiveBayesPrediction(inst,
                        this.observedClassDistribution,
                        this.attributeObservers);
            }
            return super.getClassVotes(inst, ht);
        }

        @Override
        public void disableAttribute(int attIndex) {
            // should not disable poor atts - they are used in NB calc
        }
    }

    public RandomHoeffdingTreeNB() {
        this.removePoorAttsOption = null;
    }

    @Override
    protected LearningNode newLearningNode(double[] initialClassObservations) {
        return new LearningNodeNB(initialClassObservations);
    }
}
