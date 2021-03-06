vecchioApp.controller('VecModalSelectUserListCtrl', [
	"$uibModalInstance",
	"groupId",
	"vecUserResource",
	function ($uibModalInstance, groupId, vecUserResource) {
		var $ctrl = this;
		$ctrl.groupId = groupId;
		$ctrl.checkAll = false;
		$ctrl.vecStateObjects = [];
		$ctrl.vecObjects = [];
		$ctrl.vecUsers = vecUserResource.query({}, function () {
			angular.forEach($ctrl.vecUsers, function (vecUser, key) {
				$ctrl.vecStateObjects[vecUser.username] = false;
				$ctrl.vecObjects[vecUser.username] = vecUser;
			});
		});

		$ctrl.itemSelected = false;
		$ctrl.ok = function () {			
			var objects = [];
            for (var stateKey in $ctrl.vecStateObjects) {
				console.log($ctrl.vecStateObjects[stateKey]);
                if ($ctrl.vecStateObjects[stateKey] === true) {
					objects.push($ctrl.vecObjects[stateKey]);
                }
			}
			angular.forEach(objects, function (vecUser, key) {
				console.log(vecUser.name)
			});
			$uibModalInstance.close(objects);
		};

		$ctrl.cancel = function () {
			$ctrl.removeInstance = false;
			$uibModalInstance.dismiss('cancel');
		};

		$ctrl.checkSomeItemSelected = function () {
			$ctrl.itemSelected = false;
			for (var stateKey in $ctrl.vecStateObjects) {
				if ($ctrl.vecStateObjects[stateKey]) {
					$ctrl.itemSelected = true;
				}
			}
			console.log("checkSomeItemSelected");
			for (var stateKey in $ctrl.vecStateObjects) {
				console.log($ctrl.vecStateObjects[stateKey]);
            }
		}
		$ctrl.selectEverything = function () {
			if ($ctrl.checkAll) {
				for (var stateKey in $ctrl.vecStateObjects) {
					$ctrl.vecStateObjects[stateKey] = true;
				}
			}
			else {
				for (var stateKey in $ctrl.vecStateObjects) {
					$ctrl.vecStateObjects[stateKey] = false;
				}
			}
			$ctrl.checkSomeItemSelected();
		}
	}]);
