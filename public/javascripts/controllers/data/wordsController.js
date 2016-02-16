edflow

    .controller("wordsController", function ($scope, $rootScope, $http) {

        $scope.i = 0;

        //$rootScope.selectCategory({title: "Words"});

        $scope.next = function () {
            console.log("next")
            $scope.i++;
            console.log($scope.i)
            console.log($rootScope.items)
            $scope.$apply();
        }

        document.onkeydown = checkKey;

        function checkKey(e) {

            e = e || window.event;

            if (e.keyCode == '37') {
                $scope.next();
            }
            else if (e.keyCode == '39') {
                $scope.next();
            }

        }
    })
