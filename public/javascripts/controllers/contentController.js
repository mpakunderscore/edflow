edflow

    .controller("contentController", function ($scope, $rootScope, $http) {

        console.log("contentController")

        $scope.selectOneCategory = function (title) {

            var category = {title: title}

            $rootScope.selectedCategories = [];
            $rootScope.selectCategory(category);
        }First
    })