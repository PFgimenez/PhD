#!/bin/sh

(cd ../.. ; ./run.sh Recom2 cluster 1 naif datasets/renault_medium 10 -e | tee experiments/exp11/naif-cl1-medium)
(cd ../.. ; ./run.sh Recom2 cluster 2 naif datasets/renault_medium 10 -e | tee experiments/exp11/naif-cl2-medium)
(cd ../.. ; ./run.sh Recom2 cluster 3 naif datasets/renault_medium 10 -e | tee experiments/exp11/naif-cl3-medium)
(cd ../.. ; ./run.sh Recom2 cluster 1 jointree datasets/renault_medium 10 -e | tee experiments/exp11/jointree-cl1-medium)
(cd ../.. ; ./run.sh Recom2 cluster 2 jointree datasets/renault_medium 10 -e | tee experiments/exp11/jointree-cl2-medium)
(cd ../.. ; ./run.sh Recom2 cluster 3 jointree datasets/renault_medium 10 -e | tee experiments/exp11/jointree-cl3-medium)
notify-send  'An experiment just completed'
