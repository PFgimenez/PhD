#!/bin/sh

(cd ../.. ; ./run.sh Recom2 1 v-naif constant 10 datasets/renault_medium 10 -e -s 10 | tee experiments/exp14/vnaif-10-medium-10s)
(cd ../.. ; ./run.sh Recom2 1 v-naif constant 20 datasets/renault_medium 10 -e -s 10 | tee experiments/exp14/vnaif-20-medium-10s)
(cd ../.. ; ./run.sh Recom2 1 v-naif constant 50 datasets/renault_medium 10 -e -s 10 | tee experiments/exp14/vnaif-50-medium-10s)
(cd ../.. ; ./run.sh Recom2 1 v-naif constant 200 datasets/renault_medium 10 -e -s 10 | tee experiments/exp14/vnaif-200-medium-10s)
(cd ../.. ; ./run.sh Recom2 1 v-naif constant 2000 datasets/renault_medium 10 -e -s 10 | tee experiments/exp14/vnaif-2000-medium-10s)
(cd ../.. ; ./run.sh Recom2 1 v-naif inverse 1000 datasets/renault_medium 10 -e -s 10 | tee experiments/exp14/vnaif-inverse-1000-medium-10s)
(cd ../.. ; ./run.sh Recom2 1 v-naif inverse 750 datasets/renault_medium 10 -e -s 10 | tee experiments/exp14/vnaif-inverse-750-medium-10s)
(cd ../.. ; ./run.sh Recom2 1 jointree hc datasets/renault_medium 10 -e -s 10| tee experiments/exp14/jointree-medium-10s)
notify-send  'An experiment just completed'
