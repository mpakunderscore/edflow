edflow

    .controller("wikipediaController", function ($scope, $rootScope, $http) {

        $scope.path = "Wikipedia";

        $rootScope.selectCategory({title: "Wikipedia"});
    })
