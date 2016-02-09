edflow

    .controller("contentController", function ($scope, $rootScope, $http) {

        $scope.selectOneCategory = function (title) {

            var category = {title: title}

            $rootScope.selectedCategories = [];
            $rootScope.selectCategory(category);
        }
    })