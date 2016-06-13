edflow

    .controller("contentController", function ($rootScope, $state) {

        var path = $state.current.name;

        $rootScope.selectCategory({title: path});

        console.log("selectCategory: " + path)
    })
